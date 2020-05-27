package com.ad.screen.server.event;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.service.DistributeTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 见 {@link AllocateEvent}
 *
 * @author wezhyn
 * @since 03.27.2020
 */
@Component
@Slf4j
public class AllocateEventListener implements ApplicationListener<AllocateEvent> {

    @Autowired
    private DistributeTaskService distributeTaskService;

    @Override
    public void onApplicationEvent(AllocateEvent event) {
        EquipTask equipTask=event.getEquipTask();
        List<PooledIdAndEquipCache> availableEquips=event.getPooledIdAndEquipCaches();
        int availableNum=equipTask.getAvailableAllocateNum();
        int defaultNumPerEquip=equipTask.getAvailableAllocateNum()/equipTask.getDeliverNum();
        int length=availableEquips.size();
        if (event.getType()==AllocateType.COMPENSATE || distributeTaskService.checkRunningAndPut(equipTask.getTaskKey())) {
            for (int i=0; i < length; i++) {
                int numPerTask=i==length - 1 ? availableNum : defaultNumPerEquip;
                final PooledIdAndEquipCache entry=availableEquips.get(i);
                final Task task=createTask(equipTask, entry, numPerTask);
                availableNum-=defaultNumPerEquip;
                entry.insertTask(task);
            }
        } else {
            for (PooledIdAndEquipCache availableEquip : availableEquips) {
                availableEquip.restIncr(equipTask.getRate());
            }
            log.warn("{} is running", equipTask.getTaskKey());
        }
    }

    /**
     * 创建设备分配任务信息
     *
     * @param taskMessage 支付成功信息
     * @return 分配到每个设备的数据
     */
    public Task createTask(EquipTask taskMessage, PooledIdAndEquipCache equip, int numPerTask) {
        //组合广告内容
//         成功支付订单其数据是正好匹配
        return Task.TaskBuilder.aTask()
                .taskKey(taskMessage.getTaskKey())
                .rate(taskMessage.getRate())
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
