package com.ad.admain.mq.order;

import com.ad.admain.controller.account.impl.SocialUserService;
import com.ad.admain.controller.account.user.SocialType;
import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.BillInfoService;
import com.ad.admain.controller.pay.impl.RefundBillInfoServiceImpl;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.controller.pay.to.PayType;
import com.ad.admain.pay.TradeStatus;
import com.ad.launch.order.TaskMessage;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 05.08.2020
 */
@RocketMQTransactionListener
public class SuccessOrderProduceListener implements RocketMQLocalTransactionListener {


    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private SocialUserService socialUserService;
    @Autowired
    private BillInfoService billInfoService;
    @Autowired
    private RefundBillInfoServiceImpl refundBillInfoService;


    private Cache<String, Object> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    public static String getTransId(Message msg) {
        return (String) msg.getHeaders().get("rocketmq_TRANSACTION_ID");
    }

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        if (arg instanceof TaskMessage) {
            TaskMessage message = (TaskMessage) arg;
            cache.put(getTransId(msg), message);
            return handleOrder(getTransId(msg), message);
        } else if (arg instanceof UserAuthMessage) {
            cache.put(getTransId(msg), arg);
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        final Object obj = cache.getIfPresent(getTransId(msg));

        if (obj != null) {
            if (obj instanceof TaskMessage) {
                return handleOrder(getTransId(msg), (TaskMessage) obj);
            } else if (obj instanceof UserAuthMessage) {
                UserAuthMessage authMessage = (UserAuthMessage) obj;
                return billInfoService.getByOrderId(authMessage.getOrderId())
                        .filter(b -> b.getTradeStatus() == TradeStatus.TRADE_SUCCESS)
                        .map(b -> {
                            SocialType type;
                            switch (b.getPayType()) {
                                case ALI_PAY: {
                                    type = SocialType.ALIPAY;
                                    break;
                                }
                                case WECHAT: {
                                    type = SocialType.WECHAT;
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("未知支付来源");
                                }
                            }
                            if (refundBillInfoService.refund(authMessage.getOrderId(), authMessage.getUid(),
                                    OrderStatus.SUCCESS_PAYMENT, 0.01, "验证成功", PayType.ALI_PAY)) {
                                socialUserService.bindUser(authMessage.getUid(), b.getBuyerId(), type);
                            }
                            cache.invalidate(getTransId(msg));
                            return RocketMQLocalTransactionState.ROLLBACK;
                        })
                        .orElse(RocketMQLocalTransactionState.UNKNOWN);
            }
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    public RocketMQLocalTransactionState handleOrder(String transId, TaskMessage message) {
        final AdOrder savedOrder = adOrderService.findById(message.getOid());
        switch (savedOrder.getVerify()) {
            case PASSING_VERIFY: {
                cache.invalidate(transId);
                if (savedOrder.getOrderStatus().getNumber() >= 1 && savedOrder.getOrderStatus().getNumber() <= 3) {
                    return RocketMQLocalTransactionState.COMMIT;
                } else {
                    return RocketMQLocalTransactionState.ROLLBACK;
                }
            }
            case WAIT_VERITY: {
                return RocketMQLocalTransactionState.UNKNOWN;
            }
            default: {
                cache.invalidate(transId);
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        }
    }
}
