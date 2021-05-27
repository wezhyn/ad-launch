package com.ad.admain.mq.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ad.admain.controller.pay.impl.OrderServiceImpl;
import com.ad.admain.controller.pay.impl.RefundBillInfoServiceImpl;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.controller.pay.to.PayType;
import com.ad.admain.controller.pay.to.RefundBillInfo;
import com.ad.launch.order.CompleteTaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
@RocketMQMessageListener(topic = "order-topic", consumerGroup = "complete-order-group",
    selectorExpression = "completed-order", consumeThreadMax = 2)
@Component
@Slf4j
public class CompleteOrderListener implements RocketMQListener<CompleteTaskMessage> {

    private final OrderServiceImpl orderService;
    private final RefundBillInfoServiceImpl refundBillInfoService;

    public CompleteOrderListener(OrderServiceImpl orderService, RefundBillInfoServiceImpl refundBillInfoService) {
        this.orderService = orderService;
        this.refundBillInfoService = refundBillInfoService;
    }

    @Override
    public void onMessage(CompleteTaskMessage completeTaskMessage) {
        final AdOrder completeOrder = orderService.findById(completeTaskMessage.getOrderId());
        final Double cost = completeTaskMessage.getOrderCost();
        if (completeOrder == null || completeOrder.getOrderStatus().getNumber() < 1) {
            log.error("订单异常: " + completeTaskMessage);
            return;
        }
        orderService.modifyOrderStatus(completeTaskMessage.getOrderId(), OrderStatus.EXECUTION_COMPLETED);
        final double refundAmount = BigDecimal.valueOf(completeOrder.getTotalAmount())
            .subtract(new BigDecimal(cost)).doubleValue();
        RefundBillInfo refundBill = new RefundBillInfo.RefundBillInfoBuilder()
            .orderId(completeTaskMessage.getOrderId())
            .operatorId("System")
            .buyerId(completeOrder.getUid().toString())
            .gmtCreate(LocalDateTime.now())
            .gmtPayment(LocalDateTime.now())
            .refundFee(refundAmount)
            .refundReason("剩余尾款退还")
            .payType(PayType.ALI_PAY)
            .totalAmount(completeOrder.getTotalAmount()).build();
        refundBillInfoService.save(refundBill);
    }
}
