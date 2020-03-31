package com.ad.screen.server.mq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 03.28.2020
 */
@Component
public class SchedulerServerHeartProducer {

    /**
     * 每4分钟发送一条心跳帧
     */
    public static final Long FIXED_DELAY=240*1000L;
    private final RocketMQTemplate rocketMQTemplate;
    @Value("${heart.producer.destination}")
    private String heartDestination;

    public SchedulerServerHeartProducer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate=rocketMQTemplate;
    }

    @Scheduled(fixedRate=240*1000L)
    public void send() {
        final ServerHeartMessage message=ServerHeartMessage.create();
        rocketMQTemplate.sendOneWayOrderly(heartDestination, new GenericMessage<>(message), message.getIdentify());
    }

}
