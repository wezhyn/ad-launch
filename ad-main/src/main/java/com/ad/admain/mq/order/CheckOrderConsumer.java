package com.ad.admain.mq.order;

import com.ad.admain.controller.pay.AdOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
@RocketMQMessageListener(topic = "order-topic", consumerGroup = "cancel-order-group",
        selectorExpression = "cancel-order")
@Component
public class CheckOrderConsumer implements RocketMQListener<CheckOrderMessage> {

    @Autowired
    private AdOrderService orderService;


    @Override
    public void onMessage(CheckOrderMessage payload) {
        orderService.trySuccessOrder(payload.getOrderId(), payload.getUid());
    }


}
