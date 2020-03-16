package com.ad.screen.server.mq;

import com.ad.screen.server.entity.FailTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName FailTaskConsumer
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 10:33
 * @Version V1.0
 **/
@RocketMQMessageListener(
        topic="fail_task_topic",
        consumerGroup="fail_task_consumers",
        selectorExpression="*"
)
@Slf4j
@Component
public class FailTaskListener implements RocketMQListener<FailTask> {

    @Override
    public void onMessage(FailTask message) {
        log.info("收到任务失败消息");

    }
}
