package com.ad.screen.server.mq;

import com.ad.screen.server.FailTaskService;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.exception.InsufficientException;
import com.ad.screen.server.handler.ScreenProtocolCheckInboundHandler;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.List;
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
@Component
@Slf4j
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
        int onlinenum = pooledIdAndEquipCacheService.count();
        if (onlinenum<1){
            throw new InsufficientException("当前在线车辆数目小于1");
        }
        //收到的失败任务消息
        HashMap<Long, PooledIdAndEquipCache> scopeMap = distributeTaskI.scopeEquips(message.getLongitude(),message.getLatitude(),message.getScope());
        HashMap<Long, PooledIdAndEquipCache> cache =  distributeTaskI.scopeAvailableFreeEquips(scopeMap,message.getRate());
        //目前没有这么多的在线车辆数,退出
        if (cache==null||cache.size() < 1) {
            throw new InsufficientException("目前没有这么多的在线车辆数");
        }

        Integer rate = message.getRate();
        Iterator<Map.Entry<Long,PooledIdAndEquipCache>> iterator =  cache.entrySet().iterator();
        Map.Entry<Long, PooledIdAndEquipCache> map = iterator.next();
        Long pooledId = map.getKey();
        PooledIdAndEquipCache pooledIdAndEquipCache = map.getValue();
        Channel channel = idChannelPool.getChannel(pooledId);
        HashMap<Integer, Task> received = channel.attr(ScreenChannelInitializer.TASK_MAP).get();
        if (received==null){
            Integer numPerEquip = message.getRepeatNum()/rate;
            for (int i =1;i<=rate;i++) {
                received = new HashMap<Integer, Task>();
                Task task = Task.builder()
                        .uid(pooledIdAndEquipCache.getEquipment().getUid())
                        .view(message.getView())
                        .repeatNum(message.getRepeatNum() / message.getRate())
                        .entryId(i)
                        .sendIf(false)
                        .oid(message.getId().getOid())
                        .verticalView(message.isVerticalView())
                        .build();
                received.put(i, task);
            }
        }
        else {
            Set<Integer> keySet = received.keySet();
            int addNum = rate.intValue();
            for (int j = 1; addNum > 0 && j <= 25; j++) {
                if (!keySet.contains(j)) {
                    Task task = Task.builder()
                            .uid(pooledIdAndEquipCache.getEquipment().getUid())
                            .view(message.getView())
                            .repeatNum(message.getRepeatNum() / message.getRate())
                            .entryId(j)
                            .sendIf(false)
                            .oid(message.getId().getOid())
                            .verticalView(message.isVerticalView())
                            .build();
                    received.put(j, task);
                    addNum--;
                }
            }
        }
        //同步数据库并更新缓存
        failTaskService.remove(message.getId());
        channel.attr(ScreenChannelInitializer.TASK_MAP).set(received);
        pooledIdAndEquipCache.setRest(pooledIdAndEquipCache.getRest()-rate);
        pooledIdAndEquipCacheService.setValue(channel.attr(ScreenProtocolCheckInboundHandler.EQUIPMENT).get().getKey(),pooledIdAndEquipCache);
    }
}
