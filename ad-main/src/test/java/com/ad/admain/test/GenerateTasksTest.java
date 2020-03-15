package com.ad.admain.test;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * @ClassName GenerateTasksTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/14 12:42
 * @Version V1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenerateTasksTest {
@Autowired
    RocketMQTemplate rocketMQTemplate;

    @Test
    public void generateTasks() {

//        rocketMQTemplate.asyncSend("task_topic",);

    }

}