package com.ad.screen.server.mq;

import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.entity.ResumeRecord;
import com.ad.screen.server.event.LocalResumeServerListener;
import com.ad.screen.server.service.EquipTaskService;
import com.ad.screen.server.service.ResumeRecordService;
import com.google.common.cache.*;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 03.28.2020
 */
@RocketMQMessageListener(
        topic="${heart.producer.destination}",
        consumerGroup="heart_beat_consumer",
        selectorExpression="*",
        messageModel=MessageModel.BROADCASTING
)
@Component
public class SchedulerServerHeartListener implements RocketMQListener<ServerHeartMessage>, RocketMQPushConsumerLifecycleListener {

    public static final Integer MAX_EXPIRED_TIME=20;
    private final GlobalIdentify globalIdentify;

    private Cache<String, LocalDateTime> cache;

    public SchedulerServerHeartListener(GlobalIdentify globalIdentify,
                                        ResumeRecordService resumeRecordService,
                                        EquipTaskService equipTaskService,
                                        LocalResumeServerListener resumeServerListener) {
        this.globalIdentify=globalIdentify;
        cache=CacheBuilder.newBuilder()
                .expireAfterWrite(MAX_EXPIRED_TIME, TimeUnit.MINUTES)
                .removalListener(new ServerHeartRemovalListener(resumeRecordService, equipTaskService, resumeServerListener))
                .build();
    }


    @Override
    public void onMessage(ServerHeartMessage message) {
        if (!message.getIdentify().equals(globalIdentify.getId())) {
//            收到其他服务器的心跳帧信息
            cache.put(message.getIdentify(), message.getSendTime());
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
    }

    public static class ServerHeartRemovalListener implements RemovalListener<String, LocalDateTime> {

        private final ResumeRecordService resumeRecordService;
        private final EquipTaskService equipTaskService;
        private final LocalResumeServerListener localResumeServerListener;

        public ServerHeartRemovalListener(ResumeRecordService resumeRecordService, EquipTaskService equipTaskService, LocalResumeServerListener localResumeServerListener) {
            this.resumeRecordService=resumeRecordService;
            this.equipTaskService=equipTaskService;
            this.localResumeServerListener=localResumeServerListener;
        }

        @Override
        public void onRemoval(RemovalNotification<String, LocalDateTime> notification) {
            if (notification.getCause()==RemovalCause.EXPIRED && notification.getValue().plusMinutes(MAX_EXPIRED_TIME).isBefore(LocalDateTime.now())) {
//                转移当前故障机器的调度任务
                final Optional<ResumeRecord> crashServerRecord=resumeRecordService.getById(notification.getKey());
                int lastRecord=crashServerRecord.map(ResumeRecord::getLastResumeId).orElse(0);
                try {
                    Thread.sleep(new Random().nextInt((int) (SchedulerServerHeartProducer.FIXED_DELAY/2)));
                } catch (InterruptedException ignore) {
                }
                equipTaskService.transferCrashServer(notification.getKey(), lastRecord);
                localResumeServerListener.updateResumeCount(lastRecord);
            }
        }
    }

}
