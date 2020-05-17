package com.ad.admain.mq.order;

import com.ad.launch.order.TaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 检查订单状态
 *
 * @author wezhyn
 * @since 02.29.2020
 */
@Service
@Slf4j
public class CheckOrderProduceImpl implements CheckOrderStatueProduceI, PaymentOrderProduceI {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void checkOrder(CheckOrderMessage message) {

        rocketMQTemplate.asyncSend(CheckOrderMessage.MESSAGE_TOPIC, new GenericMessage<>(message),
                new com.ad.admain.mq.order.CommonSendCallback<>(message), 3000, 16);
    }

    @Override
    public void authOrder(UserAuthMessage message) {
        rocketMQTemplate.sendMessageInTransaction(UserAuthMessage.MESSAGE_TOPIC, new GenericMessage<>(message), message);
    }

    @Override
    public void paymentOrder(TaskMessage orderMessage) {
        final TransactionSendResult topic = rocketMQTemplate.sendMessageInTransaction("task_message_topic",
                new GenericMessage<>(orderMessage), orderMessage);
    }


}
