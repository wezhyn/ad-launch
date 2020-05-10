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

import java.util.List;
import java.util.concurrent.ExecutorService;
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
    /**
     * 修改为使用内置锁进行保护
     */
    private int count;

    private final DistributeTaskService distributeTaskService;

    private ExecutorService executorService;

    public LocalResumeServerListener(EquipTaskService equipTaskService, CompletionService completionService, ResumeRecordService resumeRecordService, DistributeTaskI distributeTask,
                                     ApplicationEventPublisher applicationEventPublisher,
                                     DistributeTaskService distributeTaskService,
                                     @Qualifier(value = "resume_server") ExecutorService executorService) {
        this.equipTaskService = equipTaskService;
        this.executorService = executorService;
        this.completionService = completionService;
        this.resumeRecordService = resumeRecordService;
        this.distributeTask = distributeTask;
        this.applicationEventPublisher = applicationEventPublisher;
        this.distributeTaskService = distributeTaskService;
        this.count = 0;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("本地重启服务启动");
        setCount(resumeRecordService.resumeRecord());
        executorService.submit(() -> new LocalResumeServer(this, equipTaskService, completionService, distributeTask,
                applicationEventPublisher, distributeTaskService, executorService));
    }


    /**
     * 用于故障节点转移
     *
     * @param crashCount crashCount
     */
    public synchronized void updateResumeCount(int crashCount) {
        if (crashCount < getCount()) {
            setCount(crashCount);
        }
    }

    public synchronized int getCount() {
        return this.count;
    }

    public synchronized void setCount(Integer count) {
        this.count = count;
    }

    public synchronized int getAndIncrement() {
        this.count += 1;
        return this.count;
    }

    @Slf4j
    private static class LocalResumeServer implements Runnable {
        private final LocalResumeServerListener listener;
        private final EquipTaskService equipTaskService;
        private final CompletionService completionService;
        private final DistributeTaskI distributeTask;
        private final ApplicationEventPublisher applicationEventPublisher;
        private final DistributeTaskService distributeTaskService;
        private final ExecutorService service;


        public LocalResumeServer(LocalResumeServerListener listener, EquipTaskService equipTaskService,
                                 CompletionService completionService, DistributeTaskI distributeTask,
                                 ApplicationEventPublisher applicationEventPublisher,
                                 DistributeTaskService distributeTaskService,
                                 ExecutorService service
        ) {
            this.listener = listener;
            this.equipTaskService = equipTaskService;
            this.completionService = completionService;
            this.distributeTask = distributeTask;
            this.applicationEventPublisher = applicationEventPublisher;
            this.distributeTaskService = distributeTaskService;
            this.service = service;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (listener) {
                        final List<EquipTask> tasks = equipTaskService.nextPreparedResume(listener.getCount(), DEFAULT_RESUME_STEP);
                        if (tasks.size() == 0) {
                            service.submit(this);
                            break;
                        }
                        int lastId = 0;
                        for (int i = 0; i < tasks.size(); ) {
                            EquipTask task = tasks.get(i);
                            lastId = task.getId();
                            if (distributeTaskService.checkRunning(task.getTaskKey())) {
                                if (i == tasks.size() - 1) {
                                    break;
                                }
                                i++;
                                continue;
                            }
                            int orderId = task.getTaskKey().getOid();
//                        转存redis中的数据到数据库,自此，一个 EquipTask 的最终信息都已经保存在了数据库中
                            Integer additionalNum = completionService.forOrderTotal(orderId);
                            task.setExecutedNum(task.getExecutedNum() + additionalNum);
//                    检查当前订单是否已经完成
                            if (task.getExecutedNum().equals(task.getTotalNum())) {
                                equipTaskService.checkTaskExecuted(task.getId());
                                distributeTaskService.remove(task.getTaskKey());
                                i++;
                                continue;
                            }
                            List<PooledIdAndEquipCache> list = null;
                            while (list == null || list.size() < task.getDeliverNum()) {
                                list = distributeTask.availableEquips(task);
                                if (list == null || list.size() < task.getDeliverNum()) {
                                    TimeUnit.SECONDS.sleep(1);
                                    Thread.yield();
                                }
                            }
                            applicationEventPublisher.publishEvent(new AllocateEvent(this, AllocateType.RESUME, task, list));
                            log.info("恢复{}", task.getId());
                            i++;
                        }
                        if (lastId - 1 > listener.getCount()) {
                            listener.setCount(lastId);
                        } else {
                            listener.getAndIncrement();
                        }
                    }
                } catch (InterruptedException ignore) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
