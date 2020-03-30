package com.ad.screen.server.mq;

import com.ad.launch.order.TaskMessage;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.event.AllocateEvent;
import com.ad.screen.server.event.DistributeTaskI;
import com.ad.screen.server.exception.InsufficientException;
import com.ad.screen.server.service.EquipTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 支付成功订单消费
 *
 * @Description //mq任务消息监听
 * @Date 2020/3/6 23:15
 **/
@RocketMQMessageListener(
        topic="task_message_topic",
        consumerGroup="task_message_consumers",
        selectorExpression="*"
)
@Component
@Slf4j
public class PayMessageListener implements RocketMQListener<TaskMessage> {
    @Autowired
    DistributeTaskI distributeTaskI;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;

    @Autowired
    private EquipTaskService equipTaskService;
    @Autowired
    private GlobalIdentify globalIdentify;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void onMessage(TaskMessage taskMessage) {
        final EquipTask equipTask=createEquipTask(taskMessage);
        Integer deliverNum=taskMessage.getDeliverNum();
        equipTaskService.saveAndCheckOrder(equipTask);
        //目前区域内可用符合订单要求的车辆数小于订单要求投放的车辆数，退出
        List<PooledIdAndEquipCache> available=distributeTaskI.availableEquips(equipTask);
        if (available.size() < deliverNum) {
            throw new InsufficientException("区域内可用车辆数目小于订单要求");
        }
        applicationEventPublisher.publishEvent(new AllocateEvent(this, false, equipTask, available));
    }


    private EquipTask createEquipTask(TaskMessage taskMessage) {
        return EquipTask.EquipTaskBuilder.anEquipTask()
                .deliverNum(taskMessage.getDeliverNum())
                .latitude(taskMessage.getLatitude())
                .longitude(taskMessage.getLongitude())
                .rate(taskMessage.getRate())
                .scope(taskMessage.getScope())
                .screenView(String.join(".", taskMessage.getProduceContext()))
                .taskKey(new TaskKey(taskMessage.getOid(), taskMessage.getUid()))
                .totalNum(taskMessage.getTotalNum())
                .vertical(taskMessage.getVertical())
                .executedNum(0)
                .workIdentity(globalIdentify.getId())
                .build();
    }
}
