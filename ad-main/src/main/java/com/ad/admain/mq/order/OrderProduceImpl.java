package com.ad.admain.mq.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
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
                new CommonSendCallback<>(message), 3000, 16);
    }

    @Override
    public void paymentOrder(PaymentOrderMessage orderMessage) {
        rocketMQTemplate.asyncSend(PaymentOrderMessage.TOPIC_TAG, orderMessage,
                new CommonSendCallback<>(orderMessage));
    }

    public static class CommonSendCallback<T> implements SendCallback {

        private final T message;

        protected CommonSendCallback(T message) {
            this.message=message;
        }

        @Override
        public void onSuccess(SendResult sendResult) {
            log.debug("send cancel order : {} success", sendResult.getMsgId());
        }

        @Override
        public void onException(Throwable e) {
            // todo: 保存失败信息，补偿机制
            log.error("send message error: {}", message);
        }
    }
}
