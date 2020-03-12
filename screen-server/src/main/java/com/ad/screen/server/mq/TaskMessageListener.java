package com.ad.screen.server.mq;

import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description //mq任务消息监听
 * @Date 2020/3/6 23:15
 **/
@RocketMQMessageListener(
        topic="task_topic",
        consumerGroup="task_consumers",
        selectorExpression="*"
)
@Component
@Slf4j
public class TaskMessageListener implements RocketMQListener<List<Task>> {
    @Autowired
    IdChannelPool idChannelPool;

    @Override
    public void onMessage(List<Task> tasks) {
        Long pooledId=tasks.get(0).getPooledId();
        log.debug("收到消息{}", pooledId);
        Channel channel=idChannelPool.getChannel();
        List<Task> received=channel.attr(ScreenChannelInitializer.TASK_LIST).get();
        for (int i=0; i < tasks.size(); i++) {
            received.add(tasks.get(i));
        }
        channel.attr(ScreenChannelInitializer.TASK_LIST).set(received);
    }
}
