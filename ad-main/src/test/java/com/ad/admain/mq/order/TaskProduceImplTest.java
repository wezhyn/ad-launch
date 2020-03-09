package com.ad.admain.mq.order;

import com.ad.admain.screen.entity.FailTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @ClassName TaskProduceImplTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 11:18
 * @Version V1.0
 **/
@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class TaskProduceImplTest {
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Test
    public void testSend(){
        FailTask failTask = FailTask.builder()
                .id(1)
                .orderId(1)
                .uid(2)
                .num(100)
                .build();
        rocketMQTemplate.asyncSend("fail-task", failTask, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.debug("发送成功");
                }

            @Override
            public void onException(Throwable throwable) {
                log.debug("发送失败");
            }
        });    }
    @Test
    public void sendTask() {
    }

    @Test
    public void countOnlineNums() {
    }

    @Test
    public void freeEquips() {
    }

    @Test
    public void scopeFreeEquips() {
    }

    @Test
    public void scopeAvailableFreeEquips() {
    }
}