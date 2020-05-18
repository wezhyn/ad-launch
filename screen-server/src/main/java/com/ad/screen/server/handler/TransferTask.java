package com.ad.screen.server.handler;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.event.AllocateEvent;
import com.ad.screen.server.event.AllocateType;
import com.ad.screen.server.event.DistributeTaskI;
import com.ad.screen.server.event.LocalResumeServerListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
@Slf4j
public class TransferTask implements Runnable {
    /**
     * 当重复太多次后，丢弃当前任务，由重启线程恢复当前订单
     */
    public static final Integer MAX_RETRY = 5;
    private EquipTask task;
    private DistributeTaskI distributeTask;
    private ApplicationEventPublisher applicationEventPublisher;
    private int retryNum = 0;
    private LocalResumeServerListener resumeServerListener;


    public TransferTask(EquipTask task, DistributeTaskI distributeTask,
                        ApplicationEventPublisher applicationEventPublisher,
                        LocalResumeServerListener resumeServerListener) {
        this.task = task;
        this.distributeTask = distributeTask;
        this.applicationEventPublisher = applicationEventPublisher;
        this.resumeServerListener = resumeServerListener;
    }

    @Override
    public void run() {
        try {
            while (this.retryNum < MAX_RETRY) {
                List<PooledIdAndEquipCache> availableEquips = distributeTask.availableEquips(task);
                if (availableEquips.size() < task.getDeliverNum()) {
                    Thread.sleep(scheduleTime());
                } else {
                    applicationEventPublisher.publishEvent(new AllocateEvent(this, AllocateType.COMPENSATE, task, availableEquips));
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//                  丢弃当前任务
        resumeServerListener.updateResumeCount(task.getId() == null ? 0 : task.getId());
        log.debug("重置本地恢复服务");
    }


    public int scheduleTime() {
        retryNum++;
        if (retryNum < MAX_RETRY) {
            return (int) Math.pow(2, retryNum);
        } else {
            return (int) Math.pow(2, MAX_RETRY);
        }
    }
}
