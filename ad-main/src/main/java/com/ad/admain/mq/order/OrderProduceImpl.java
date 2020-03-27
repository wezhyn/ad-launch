package com.ad.admain.mq.order;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.launch.order.TaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrderProduceImpl implements CheckOrderStatueProduceI, PaymentOrderProduceI {

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private AdOrderService orderService;


    @Override
    public void checkOrder(CheckOrderMessage message) {

        rocketMQTemplate.asyncSend(CheckOrderMessage.MESSAGE_TOPIC, new GenericMessage<>(message),
                new com.ad.admain.mq.order.CommonSendCallback<>(message), 3000, 16);
    }

    @Override
    public void paymentOrder(TaskMessage orderMessage) {
        rocketMQTemplate.asyncSend("task_message_topic", orderMessage,
                new com.ad.admain.mq.order.CommonSendCallback<>(orderMessage));
    }


}
