package com.ad.screen.server.event;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.ResumeRecord;
import com.ad.screen.server.service.CompletionService;
import com.ad.screen.server.service.EquipTaskService;
import com.ad.screen.server.service.ResumeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 宕机重启服务
 *
 * @author wezhyn
 * @since 03.27.2020
 */
@Component
@Slf4j
public class LocalResumeServerListener implements ApplicationListener<ContextRefreshedEvent> {

    public static final Integer DEFAULT_RESUME_STEP=1;
    private final EquipTaskService equipTaskService;
    private final CompletionService completionService;
    private final ResumeRecordService resumeRecordService;
    private final DistributeTaskI distributeTask;
    private final ApplicationEventPublisher applicationEventPublisher;
    private GlobalIdentify globalIdentify=GlobalIdentify.IDENTIFY;
    private AtomicInteger count;

    private ExecutorService executorService=Executors.newSingleThreadExecutor(r->{
        final Thread thread=new Thread(r, "resume-thread");
        thread.setDaemon(true);
        return thread;
    });

    public LocalResumeServerListener(EquipTaskService equipTaskService, CompletionService completionService, ResumeRecordService resumeRecordService, DistributeTaskI distributeTask, ApplicationEventPublisher applicationEventPublisher) {
        this.equipTaskService=equipTaskService;
        this.completionService=completionService;
        this.resumeRecordService=resumeRecordService;
        this.distributeTask=distributeTask;
        this.applicationEventPublisher=applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final Optional<ResumeRecord> record=resumeRecordService.getById(globalIdentify.getId());
        log.info("本地重启服务启动");
        count=new AtomicInteger(resumeRecordService.resumeRecord());
        executorService.submit(()->{
            final List<EquipTask> tasks=equipTaskService.nextPreparedResume(count.get(), DEFAULT_RESUME_STEP);
            if (tasks.size()==0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignore) {
                }
            } else {
                for (int i=0; i < tasks.size(); ) {
                    EquipTask task=tasks.get(i);
                    try {
                        int orderId=task.getTaskKey().getOid();
//                        转存redis中的数据到数据库,自此，一个 EquipTask 的最终信息都已经保存在了数据库中
                        Integer additionalNum=completionService.forOrderTotal(orderId);
                        task.setExecutedNum(task.getExecutedNum() + additionalNum);
//                    检查当前订单是否已经完成
                        if (task.getExecutedNum().equals(task.getTotalNum())) {
                            equipTaskService.checkTaskExecuted(task.getId());
                        }
                        List<PooledIdAndEquipCache> list;
                        while (true) {
                            list=distributeTask.availableEquips(task);
                            if (list==null || list.size() < task.getDeliverNum()) {
                                try {
                                    TimeUnit.SECONDS.sleep(10);
                                } catch (InterruptedException ignore) {
                                }
                            } else {
                                break;
                            }
                        }
                        applicationEventPublisher.publishEvent(new AllocateEvent(this, true, task, list));
                        count.getAndIncrement();
                        log.info("恢复{}", task.getId());
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 用于故障节点转移
     *
     * @param crashCount crashCount
     */
    public void updateResumeCount(int crashCount) {
        if (crashCount < this.count.get()) {
            count.set(crashCount);
        }
    }
}
