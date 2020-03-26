package com.ad.screen.server.mq;

import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.FailTaskService;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.exception.InsufficientException;
import com.ad.screen.server.handler.ScreenProtocolCheckInboundHandler;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

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
        int onlinenum=pooledIdAndEquipCacheService.count();
        if (onlinenum < 1) {
            throw new InsufficientException("当前在线车辆数目小于1");
        }
        //收到的失败任务消息
        final List<PooledIdAndEquipCache> cache=distributeTaskI.availableEquips(message);
        //目前没有这么多的在线车辆数,退出
        if (cache==null || cache.size() < 1) {
            throw new InsufficientException("目前没有这么多的在线车辆数");
        }
        PooledIdAndEquipCache availableEquip=cache.get(0);
        try {
            Integer rate=message.getRate();
            Long pooledId=availableEquip.getPooledId();
            Channel channel=idChannelPool.getChannel(pooledId);
            HashMap<Integer, Task> received=channel.attr(ScreenChannelInitializer.TASK_MAP).get();
            if (received==null) {
                received=new HashMap<>(16);
                channel.attr(ScreenChannelInitializer.TASK_MAP).set(received);
            }
            int addNum=rate;
            for (int j=1; addNum > 0 && j <= 25; j++) {
                // 将任务加入对应 channel 的 received 列表中
                Task task=createTask(message, availableEquip, j);
                if (null==received.putIfAbsent(j, task)) {
                    addNum--;
                }
            }
            AdEquipment equipment=channel.attr(ScreenProtocolCheckInboundHandler.EQUIPMENT).get();
            log.info("已经为IMEI号为:{}的车辆安排了{}个任务", equipment.getKey(), rate);
            // 同步数据库并更新缓存
            failTaskService.remove(message.getId());
        } finally {
            if (availableEquip!=null) {
                availableEquip.releaseAllocate();
            }
        }
    }

    public Task createTask(FailTask message, PooledIdAndEquipCache availableEquip, int entryId) {
        return Task.builder()
                .uid(availableEquip.getEquipment().getUid())
                .view(message.getView())
                .repeatNum(message.getRepeatNum()/message.getRate())
                .entryId(entryId)
                .sendIf(false)
                .oid(message.getId().getOid())
                .verticalView(message.isVerticalView())
                .build();
    }
}
