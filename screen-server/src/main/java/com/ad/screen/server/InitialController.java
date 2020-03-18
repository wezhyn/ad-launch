package com.ad.screen.server;

import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.mq.CommonSendCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @ClassName InitialController
 * @Description 启动时默认检查是否存在未发送的失败任务，若有则将其发送到失败任务队列中
 * @Author ZLB_KAM
 * @Date 2020/3/18 0:22
 * @Version V1.0
 **/
@Controller
public class InitialController {
    @Autowired
    FailTaskService failTaskService;
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public void SendFailTasks(){

    }
}
