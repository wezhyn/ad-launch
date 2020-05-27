package com.ad.screen.server.event;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.service.CompletionService;
import com.ad.screen.server.service.DistributeTaskService;
import com.ad.screen.server.service.EquipTaskService;
import com.ad.screen.server.service.ResumeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 宕机重启服务
 *
 * @author wezhyn
 * @since 03.27.2020
 */
@Component
@Slf4j
public class LocalResumeServerListener implements ApplicationListener<ContextRefreshedEvent> {

    public static final Integer DEFAULT_RESUME_STEP = 1;
    private final EquipTaskService equipTaskService;
    private final CompletionService completionService;
    private final ResumeRecordService resumeRecordService;
    private final DistributeTaskI distributeTask;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DistributeTaskService distributeTaskService;
    private final ScheduledExecutorService executorService;

    private final LocalResumeState localResumeState;

    public LocalResumeServerListener(EquipTaskService equipTaskService, CompletionService completionService, ResumeRecordService resumeRecordService, DistributeTaskI distributeTask,
                                     ApplicationEventPublisher applicationEventPublisher,
                                     DistributeTaskService distributeTaskService,
                                     @Qualifier(value = "self_taskExecutor") ScheduledExecutorService executorService) {
        this.equipTaskService = equipTaskService;
        this.executorService = executorService;
        this.completionService = completionService;
        this.resumeRecordService = resumeRecordService;
        this.distributeTask = distributeTask;
        this.applicationEventPublisher = applicationEventPublisher;
        this.distributeTaskService = distributeTaskService;
        this.localResumeState = LocalResumeState.INSTANCE;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("本地重启服务启动");
        localResumeState.setResumeIndex(resumeRecordService.resumeRecord());
        executorService.submit(new LocalResumeServer());
    }


    private class LocalResumeServer implements Runnable {
        private static final int MAX_RETRY_NUM = 6;
        private int retryNum = 0;

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (localResumeState) {
                        final List<EquipTask> tasks = fetchRecoverTask();
                        if (tasks.size() == 0) {
                            executorService.schedule(this, scheduleTime(), TimeUnit.SECONDS);
                            return;
                        }
                        retryNum = 0;
                        int lastId = 0;
                        for (EquipTask task : tasks) {
                            lastId = task.getId();
                            if (distributeTaskService.checkRunning(task.getTaskKey())) {
                                continue;
                            }
                            Integer hasCompleted = completionService.getOrderExecutedNumInComplete(task.getTaskKey().getOid());
                            task.setExecutedNum(task.getExecutedNum() + (hasCompleted == null ? 0 : hasCompleted));
//                    检查当前订单是否已经完成
                            if (task.getExecutedNum().equals(task.getTotalNum())) {
                                if (equipTaskService.checkTaskExecuted(task.getId()) > 0) {
                                    applicationEventPublisher.publishEvent(new CompleteTaskEvent(this, task.getTaskKey().getOid()));
                                    distributeTaskService.remove(task.getTaskKey());
                                    continue;
                                }
                            }
                            List<PooledIdAndEquipCache> list = null;
                            while (list == null || list.size() < task.getDeliverNum()) {
                                list = distributeTask.availableEquips(task);
                                if (list == null || list.size() < task.getDeliverNum()) {
                                    TimeUnit.MILLISECONDS.sleep(scheduleTime() * 100);
                                    Thread.yield();
                                }
                            }
                            applicationEventPublisher.publishEvent(new AllocateEvent(this, AllocateType.RESUME, task, list));
                            retryNum = retryNum / 2;
                            log.info("恢复{}", task.getId());
                        }
                        localResumeState.setResumeIndex(lastId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public List<EquipTask> fetchRecoverTask() {
            try {
                return equipTaskService.nextPreparedResume(localResumeState.getResumeIndex(), DEFAULT_RESUME_STEP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        }

        public int scheduleTime() {
            retryNum++;
            if (retryNum < MAX_RETRY_NUM) {
                return (int) Math.pow(2, retryNum);
            } else {
                return (int) Math.pow(2, MAX_RETRY_NUM);
            }
        }
    }
}
