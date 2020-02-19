package com.ad.admain.controller.quartz.job;

import com.ad.admain.controller.quartz.dao.JobEntityRepository;
import com.ad.admain.controller.quartz.entity.JobEntity;
import com.ad.admain.controller.quartz.listener.DynamicSchedulerListener;
import com.ad.admain.controller.quartz.service.DynamicJobService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName InitializeJob
 * @Description job初始化执行类
 * @Param
 * @Author ZLB
 * @Date 2020/1/27 21:09
 * @Version 1.0
 */
@Component
@Slf4j
public class InitializeJob implements Job {
    @Autowired
    JobEntityRepository jobEntityRepository;
    @Autowired
    DynamicJobService jobService;
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    DynamicSchedulerListener schedulerListener;
    @Transactional
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("开始检查是否存在未开始的任务");
        try {
            org.quartz.Scheduler scheduler = schedulerFactoryBean.getScheduler();
            List<JobEntity> jobEntities = jobService.loadJobsByStatus("CLOSE");
            if (jobEntities.size()!=0)
            {
                for (JobEntity job :jobEntities ) {                               //从数据库中注册的所有JOB
                log.info("Job register name : {} , group : {} , cron : {}", job.getName(), job.getJobGroup(), job.getCron());
                JobDataMap map = jobService.getJobDataMap(job);
                JobKey jobKey = jobService.getJobKey(job);
                JobDetail jobDetail = jobService.getJobDetail(jobKey, job.getDescription(), map);
                job = jobService.updateJobEntity(job.setStatus("OPEN"));
                if (job.getStatus().equals("OPEN")){
                    ListenerManager listenerManager = scheduler.getListenerManager();
                    listenerManager.addSchedulerListener(schedulerListener);
                    scheduler.scheduleJob(jobDetail, jobService.getTrigger(job));
                     }
                }
            }
            else{
                log.info("目前没有可以执行的任务\n");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
