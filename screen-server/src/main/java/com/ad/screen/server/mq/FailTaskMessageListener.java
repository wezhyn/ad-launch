package com.ad.screen.server.mq;

import com.ad.screen.server.FailTaskService;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.handler.ScreenProtocolCheckInboundHandler;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName TaskMessageListener
 * @Description 监听并消费执行失败的任务
 * @Author ZLB_KAM
 * @Date 2020/3/16 19:20
 * @Version V1.0
 **/

@RocketMQMessageListener(
        topic="fail_task_topic",
        consumerGroup="fail_task_consumers",
        selectorExpression="*"
)
public class FailTaskMessageListener implements RocketMQListener<FailTask> {

    @Autowired
    IdChannelPool idChannelPool;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;
    @Autowired
    DistributeTaskI distributeTaskI;
    @Autowired
    FailTaskService failTaskService;
    @Override
    public void onMessage(FailTask message) {
        //收到的失败任务消息
       HashMap<Long, PooledIdAndEquipCache> cache =  distributeTaskI.scopeAvailableFreeEquips(message.getLongitude(),message.getLatitude(),message.getScope(),message.getRate());
       assert cache!=null;
       assert cache.size()>=1;

        Iterator<Map.Entry<Long,PooledIdAndEquipCache>> iterator =  cache.entrySet().iterator();
        Map.Entry<Long, PooledIdAndEquipCache> map = iterator.next();
        Long pooledId = map.getKey();
        PooledIdAndEquipCache equip = map.getValue();
        Channel channel = idChannelPool.getChannel(pooledId);
        List<Task> received = channel.attr(ScreenChannelInitializer.TASK_LIST).get();
        int entryId = received.size();
        for (int i =entryId;i<entryId+message.getRate();i++){
           Task task = Task.builder()
                    .uid(equip.getEquipment().getUid())
                    .view(message.getView())
                    .repeatNum(message.getRepeatNum()/message.getRate())
                    .entryId(i)
                    .sendIf(false)
                    .oid(message.getId().getOid())
                    .verticalView(message.isVerticalView())
                    .build();
            received.add(task);
            //同步数据库
            failTaskService.remove(message.getId());
        }
        channel.attr(ScreenChannelInitializer.TASK_LIST).set(received);
        pooledIdAndEquipCacheService.setValue(channel.attr(ScreenProtocolCheckInboundHandler.EQUIPMENT).get().getKey(),equip);
    }
}
