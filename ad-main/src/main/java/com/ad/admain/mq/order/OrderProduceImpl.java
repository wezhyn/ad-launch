package com.ad.admain.mq.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
@Service
@Slf4j
public class OrderProduceImpl implements CancelOrderProduceI, PaymentOrderProduceI {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void cancelOrder(CancelOrderMessage message) {
        rocketMQTemplate.asyncSend(CancelOrderMessage.MESSAGE_TOPIC, new GenericMessage<>(message),
                new com.ad.admain.mq.order.CommonSendCallback<>(message), 3000, 16);
    }

    @Override
    public void paymentOrder(PaymentOrderMessage orderMessage) {
        rocketMQTemplate.asyncSend(PaymentOrderMessage.TOPIC_TAG, orderMessage,
                new com.ad.admain.mq.order.CommonSendCallback<>(orderMessage));
    }


}
