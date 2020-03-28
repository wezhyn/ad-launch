package com.ad.screen.server.event;

import com.ad.screen.server.cache.ChannelCloseException;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.ResumeRecord;
import com.ad.screen.server.mq.DistributeTaskI;
import com.ad.screen.server.service.CompletionService;
import com.ad.screen.server.service.EquipTaskService;
import com.ad.screen.server.service.ResumeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private EquipTaskService equipTaskService;
    @Autowired
    private CompletionService completionService;
    @Autowired
    private ResumeRecordService resumeRecordService;
    @Autowired
    private DistributeTaskI distributeTask;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private GlobalIdentify globalIdentify=GlobalIdentify.IDENTIFY;
    private ExecutorService executorService=Executors.newSingleThreadExecutor(r->{
        final Thread thread=new Thread(r, "resume-thread-");
        thread.setDaemon(true);
        return thread;
    });

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final Optional<ResumeRecord> record=resumeRecordService.getById(globalIdentify.getId());
        log.info("本地重启服务启动");
        AtomicInteger count;
        count=record.map(resumeRecord->new AtomicInteger(resumeRecord.getLastResumeId()))
                .orElseGet(()->new AtomicInteger(0));
        executorService.submit(()->{
            final List<EquipTask> tasks=equipTaskService.nextPreparedResume(count.get(), DEFAULT_RESUME_STEP);
            if (tasks.size()==0) {
                executorService.shutdown();
            } else {
                for (EquipTask task : tasks) {
                    int orderId=task.getTaskKey().getOid();
//                        转存redis中的数据到数据库,自此，一个 EquipTask 的最终信息都已经保存在了数据库中
                    Integer additionalNum=completionService.forOrderTotal(orderId);
                    task.setExecutedNum(task.getExecutedNum() + additionalNum);
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
                    try {
                        applicationEventPublisher.publishEvent(new AllocateEvent(this, true, task, list));
                        log.info("恢复{}", task.getId());
                    } catch (ChannelCloseException e) {
                        tasks.add(task);
                    }
                }
            }
        });
    }

}
