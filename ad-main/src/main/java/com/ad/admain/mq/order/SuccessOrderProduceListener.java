package com.ad.admain.mq.order;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.launch.order.TaskMessage;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wezhyn
 * @since 05.08.2020
 */
@RocketMQTransactionListener
public class SuccessOrderProduceListener implements RocketMQLocalTransactionListener {


    @Autowired
    private AdOrderService adOrderService;
    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();

    public static String getTransId(Message msg) {
        return (String) msg.getHeaders().get("rocketmq_TRANSACTION_ID");
    }

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        if (arg instanceof TaskMessage) {
            TaskMessage message = (TaskMessage) arg;
            cache.putIfAbsent(getTransId(msg), message);
            return handleOrder(getTransId(msg), message);
        }

        return RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        final Object obj = cache.get(getTransId(msg));
        if (obj != null) {
            if (obj instanceof TaskMessage) {
                return handleOrder(getTransId(msg), (TaskMessage) obj);
            }
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    public RocketMQLocalTransactionState handleOrder(String transId, TaskMessage message) {
        final AdOrder savedOrder = adOrderService.findById(message.getOid());
        switch (savedOrder.getVerify()) {
            case PASSING_VERIFY: {
                cache.remove(transId);
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
                cache.remove(transId);
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        }
    }
}
