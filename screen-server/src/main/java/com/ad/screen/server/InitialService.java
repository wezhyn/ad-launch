package com.ad.screen.server;

import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.mq.CommonSendCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName InitialService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/19 1:05
 * @Version V1.0
 **/
@Component
@Slf4j
public class InitialService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    FailTaskService failTaskService;
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<FailTask> failTasks = failTaskService.findAll();
        if (failTasks!=null && failTasks.size()!=0){
            for (FailTask failTask : failTasks
            ) {
                rocketMQTemplate.syncSend("fail_task_topic",failTask);
                log.info("已发送失败任务");
            }
        }
    }
}
