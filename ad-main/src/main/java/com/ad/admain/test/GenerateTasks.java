package com.ad.admain.test;

import com.ad.launch.order.TaskMessage;
import com.wezhyn.project.controller.ResponseResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName GenerateTasks
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/14 10:43
 * @Version V1.0
 **/
@Controller
@RequestMapping("/test")
public class GenerateTasks {
    @Resource
    RocketMQTemplate rocketMQTemplate;
    @RequestMapping("/order")
    @ResponseBody
    public ResponseResult generateTasks(@RequestBody TaskMessage taskMessage){
        System.out.println(taskMessage.toString());
        rocketMQTemplate.syncSend("task_message_topic",taskMessage);
        return ResponseResult.forSuccessBuilder()
                .withCode(200)
                .withMessage("消息发送成功")
                .build();
    }
}
