package com.ad.screen.server.mq;

import com.ad.launch.order.CompleteTaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
@Service
@Slf4j
public class CompleteTaskProducer implements CompleteTaskCallback {


    private final RocketMQTemplate mqTemplate;

    public CompleteTaskProducer(RocketMQTemplate mqTemplate) {
        this.mqTemplate = mqTemplate;
    }

    @Override
    public void send(CompleteTaskMessage message) {
        mqTemplate.asyncSend(CompleteTaskMessage.MESSAGE_TOPIC, new GenericMessage<>(message),
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        log.error("发送：{} 失败", message.getOrderId());
                    }
                });
    }
}
