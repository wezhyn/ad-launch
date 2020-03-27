package com.ad.screen.server.event;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Component
@Slf4j
public class AllocateEventListener implements ApplicationListener<AllocateEvent> {


    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;


    @Override
    public void onApplicationEvent(AllocateEvent event) {
        EquipTask equipTask=event.getEquipTask();
        List<PooledIdAndEquipCache> availableEquips=event.getPooledIdAndEquipCaches();
        int availableNum=equipTask.getAvailableAllocateNum();
        int numPerEquip=equipTask.getAvailableAllocateNum()/equipTask.getDeliverNum();
        int rate=equipTask.getRate();
        int defaultNumPerTask=numPerEquip/rate;
        int allocateNum=1;
        for (PooledIdAndEquipCache entry : availableEquips) {
            Channel channel=entry.getChannel();
            try {
                //取出channel已经收到的任务列表
                HashMap<Integer, Task> received=new HashMap<>(16);
                HashMap<Integer, Task> prepareReceived=channel.attr(ScreenChannelInitializer.TASK_MAP).setIfAbsent(received);
                if (prepareReceived!=null) {
                    received=prepareReceived;
                }
                int addNum=rate;
                for (int j=1; addNum > 0 && j <= 25; j++) {
                    // 将任务加入对应 channel 的 received 列表中
                    int numPerTask=allocateNum==equipTask.getRate()*equipTask.getDeliverNum() ? availableNum : defaultNumPerTask;
                    Task task=createTask(equipTask, entry, j, numPerTask);
                    if (null==received.putIfAbsent(j, task)) {
                        addNum--;
                        allocateNum++;
                        availableNum-=defaultNumPerTask;
                    }
                }
//                    任务分配完成
                log.info("已经往IMEI为:{}的channel中安排了{}个task", entry.getEquipment().getKey(), rate);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                    确保设备再次被其他任务调度
            }
        }
    }

    /**
     * 创建设备分配任务信息
     *
     * @param taskMessage 支付成功信息
     * @param entryId     taskMap 中的唯一条目编号
     * @return 分配到每个设备的数据
     */
    public Task createTask(EquipTask taskMessage, PooledIdAndEquipCache equip, int entryId, int numPerTask) {
        //组合广告内容
//         成功支付订单其数据是正好匹配

        return Task.TaskBuilder.aTask()
                .taskKey(taskMessage.getTaskKey())
                .entryId(entryId)
                .repeatNum(numPerTask)
                .deliverUserId(equip.getEquipment().getUid())
                .view(taskMessage.getScreenView())
                .verticalView(taskMessage.getVertical())
                .latitude(taskMessage.getLatitude())
                .longitude(taskMessage.getLongitude())
                .scope(taskMessage.getScope())
                .build();
    }
}
