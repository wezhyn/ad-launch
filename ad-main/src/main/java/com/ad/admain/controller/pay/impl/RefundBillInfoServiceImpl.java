package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.RefundBillInfoService;
import com.ad.admain.controller.pay.repository.RefundBillInfoRepository;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.controller.pay.to.PayType;
import com.ad.admain.controller.pay.to.RefundBillInfo;
import com.ad.admain.controller.pay.to.RefundOrder;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.TradeStatus;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.ad.admain.controller.pay.PayController.ORDER_REFUND_ALIPAY_MAPPER;

/**
 * @ClassName RefundServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/25 13:16
 * @Version 1.0
 */
@Service
public class RefundBillInfoServiceImpl extends AbstractBaseService<RefundBillInfo, Integer> implements RefundBillInfoService {

    @Autowired
    RefundBillInfoRepository refundOrderRepository;

    @Autowired
    private BillInfoServiceImpl billInfoService;
    @Autowired
    private OrderServiceImpl orderService;


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean refund(Integer orderId, Integer userId, OrderStatus originStatus, Double amount, String reason, PayType type) {
//                    并发控制委托 mysql
        try {
            if (orderService.modifyOrderStatus(orderId, OrderStatus.REFUNDED)) {
//                    第一次退款
                return billInfoService.getByOrderId(orderId)
                        .filter(b -> b.getTradeStatus() == TradeStatus.TRADE_SUCCESS)
                        .map(b -> RefundOrder.createRefundOrder(userId, amount, reason)
                                .outTradeNo(b.getAlipayTradeNo())
                                .adOrderId(b.getOrderId())
                                .build())
                        .map(r -> AliPayHolder.refundAmount(r, ORDER_REFUND_ALIPAY_MAPPER))
                        .map(r -> {
                            if (!r.isSuccess()) {
                                throw new RuntimeException("退款异常： " + r.err());
                            }
                            RefundBillInfo refundBill = new RefundBillInfo.RefundBillInfoBuilder()
                                    .orderId(orderId)
                                    .operatorId("System")
                                    .refundCurrency(r.getRefundCurrency())
                                    .refundReason(reason)
                                    .outBizNo(r.getOutTradeNo())
                                    .payType(type)
                                    .totalAmount(Double.valueOf(r.getRefundFee())).build();
                            refundOrderRepository.save(refundBill);
                            return true;
                        })
                        .orElse(false);
            }

        } catch (Exception e) {
            orderService.rollbackRefundOrderStatus(orderId, originStatus);
            throw e;
        }
        return false;
    }

    @Override
    public JpaRepository<RefundBillInfo, Integer> getRepository() {
        return refundOrderRepository;
    }
}
