package com.ad.admain.screen.mq;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.screen.IdChannelPool;
import com.ad.admain.screen.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description //mq任务消息监听
 * @Date 2020/3/6 23:15
 **/
@RocketMQMessageListener(
        topic = "task_topic",
        consumerGroup = "task_consumers",
        selectorExpression = "*"
)
@Component
@Slf4j
public class TaskMessageListener implements RocketMQListener<Task> {
    @Autowired
    IdChannelPool idChannelPool;

    @Override
    public void onMessage(Task task) {
        log.debug("收到消息{}",task.getId());
        task.getUid();
        Equipment equipment = task.getEquipment();
    }
}
