package com.ad.screen.server.service;

import com.ad.launch.order.RevenueConfig;
import com.ad.screen.server.dao.DiskCompletionRepository;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.event.CompleteTaskEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

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
    private ExecutorService executorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeNumIncr(int orderId, int driverId, int num) {
        diskCompletionRepository.createOrIncrComplete(driverId, orderId, num, RevenueConfig.revenueScope(LocalDateTime.now()));
    }

    @Override
    public void tryComplete(int orderId, int driverId) {
        Integer driverExe = diskCompletionRepository.findByAdOrderIdAndDriverId(orderId, driverId);
        if (driverExe != null) {
            equipTaskService.mergeTaskExecStatistics(new TaskKey(orderId, driverId), driverExe);
            if (equipTaskService.checkTaskExecuted(orderId) > 0) {
                executorService.submit(() -> applicationEventPublisher.publishEvent(new CompleteTaskEvent(this, orderId)));
            }
        }
    }

    @Override
    public Integer getOrderExecutedNumInComplete(Integer orderId) {
        return diskCompletionRepository.orderExecutedStatistic(orderId);
    }
}
