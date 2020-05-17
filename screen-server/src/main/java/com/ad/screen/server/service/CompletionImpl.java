package com.ad.screen.server.service;

import com.ad.screen.server.dao.DiskCompletionRepository;
import com.ad.screen.server.entity.DiskCompletion;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.event.CompleteTaskEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName CompletionImpl
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:49
 * @Version V1.0
 **/
@Service
public class CompletionImpl implements CompletionService {


    @Autowired
    private DiskCompletionRepository diskCompletionRepository;
    @Autowired
    private EquipTaskService equipTaskService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Qualifier("self_taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor executorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeNumIncr(int orderId, int driverId, int num) {
        diskCompletionRepository.createOrIncrComplete(driverId, orderId, num);
    }

    @Override
    public void tryComplete(int orderId, int driverId) {
        final DiskCompletion disk = diskCompletionRepository.findByAdOrderIdAndDriverId(orderId, driverId);
        if (disk != null) {
            final Integer totalExe = disk.getExecutedNum();
            equipTaskService.mergeTaskExecStatistics(new TaskKey(orderId, driverId), totalExe);
            if (equipTaskService.checkTaskExecuted(orderId) > 0) {
                executorService.submit(() -> applicationEventPublisher.publishEvent(new CompleteTaskEvent(disk, orderId)));
            }
        }
    }
}
