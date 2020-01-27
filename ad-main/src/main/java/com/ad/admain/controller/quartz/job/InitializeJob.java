package com.ad.admain.controller.quartz.job;

import com.ad.admain.controller.quartz.dao.JobEntityRepository;
import com.ad.admain.controller.quartz.entity.JobEntity;
import com.ad.admain.controller.quartz.service.DynamicJobService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

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
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("开始检查是否存在未开始的任务");
        try {
            org.quartz.Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (JobEntity job : jobService.loadJobsByStatus("CLOSE")) {                               //从数据库中注册的所有JOB
                log.info("Job register name : {} , group : {} , cron : {}", job.getName(), job.getJobGroup(), job.getCron());
                JobDataMap map = jobService.getJobDataMap(job);
                JobKey jobKey = jobService.getJobKey(job);
                JobDetail jobDetail = jobService.getJobDetail(jobKey, job.getDescription(), map);
                job = jobService.updateJobEntity(job.setStatus("OPEN"));
                if (job.getStatus().equals("OPEN")){
                    scheduler.scheduleJob(jobDetail, jobService.getTrigger(job));
                }
                else
                    log.info("Job jump name : {} , Because {} status is {}", job.getName(), job.getName(), job.getStatus());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
