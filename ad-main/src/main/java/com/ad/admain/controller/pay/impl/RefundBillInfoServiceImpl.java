package com.ad.admain.controller.pay.impl;

import java.time.LocalDateTime;

import com.ad.admain.controller.pay.RefundBillInfoService;
import com.ad.admain.controller.pay.repository.RefundBillInfoRepository;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.controller.pay.to.PayType;
import com.ad.admain.controller.pay.to.RefundBillInfo;
import com.ad.admain.controller.pay.to.RefundOrder;
import com.ad.admain.pay.TradeStatus;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName RefundServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/25 13:16
 * @Version 1.0
 */
@Service
public class RefundBillInfoServiceImpl extends AbstractBaseService<RefundBillInfo, Integer>
    implements RefundBillInfoService {

    @Autowired
    RefundBillInfoRepository refundOrderRepository;

    @Autowired
    private BillInfoServiceImpl billInfoService;
    @Autowired
    private OrderServiceImpl orderService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean refund(Integer orderId, Integer userId, OrderStatus originStatus, Double amount, String reason,
        PayType type) {
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
                    .map(r -> {
                        RefundBillInfo refundBill = new RefundBillInfo.RefundBillInfoBuilder()
                            .orderId(orderId)
                            .operatorId("System")
                            .buyerId(userId.toString())
                            .gmtCreate(LocalDateTime.now())
                            .gmtPayment(LocalDateTime.now())
                            .refundFee(amount)
                            .refundCurrency(r.getRefundCurrency())
                            .refundReason(reason)
                            .outBizNo(r.getOutTradeNo())
                            .payType(type)
                            .totalAmount(amount).build();
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
