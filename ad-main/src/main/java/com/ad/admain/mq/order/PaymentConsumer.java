package com.ad.admain.mq.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
@RocketMQMessageListener(consumerGroup="payment-order-group", topic="order-topic",
        selectorExpression="payment-order"
)
@Slf4j
public class PaymentConsumer implements RocketMQListener<PaymentOrderMessage> {

    @Override
    public void onMessage(PaymentOrderMessage message) {
        log.info(message.toString());
    }
}
