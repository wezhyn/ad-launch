package com.ad.admain.mq.order;

import com.ad.admain.controller.pay.impl.OrderServiceImpl;
import com.ad.admain.controller.pay.impl.RefundBillInfoServiceImpl;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.PayType;
import com.ad.launch.order.CompleteTaskMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
@RocketMQMessageListener(topic = "order-topic", consumerGroup = "complete-order-group",
        selectorExpression = "completed-order", consumeThreadMax = 2)
@Component
public class CompleteOrderListener implements RocketMQListener<CompleteTaskMessage> {

    private final OrderServiceImpl orderService;
    private final RefundBillInfoServiceImpl refundBillInfoService;

    public CompleteOrderListener(OrderServiceImpl orderService, RefundBillInfoServiceImpl refundBillInfoService) {
        this.orderService = orderService;
        this.refundBillInfoService = refundBillInfoService;
    }

    @Override
    public void onMessage(CompleteTaskMessage completeTaskMessage) {
        final Double cost = completeTaskMessage.getOrderCost();
        final AdOrder completeOrder = orderService.findById(completeTaskMessage.getOrderId());
        if (completeOrder == null || completeOrder.getOrderStatus().getNumber() < 0) {
            throw new RuntimeException("订单异常");
        }
        final double refundAmount = BigDecimal.valueOf(completeOrder.getTotalAmount()).subtract(new BigDecimal(cost)).doubleValue();
        final boolean isRefund = refundBillInfoService.refund(completeTaskMessage.getOrderId(), completeOrder.getUid(),
                completeOrder.getOrderStatus(), refundAmount, "任务结算退款", PayType.ALI_PAY);
        if (!isRefund) {
            throw new RuntimeException("退款异常");
        }
    }
}
