package com.ad.admain.controller.quartz;

import com.ad.admain.controller.quartz.test.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName Scheduler
 * @Description 调度定时任务
 * @Param
 * @Author ZLB
 * @Date 2020/1/21 22:31
 * @Version 1.0
 */
@Component
public class Scheduler {
    public static void main(String[] args) throws SchedulerException {
       org.quartz.Scheduler scheduler =  StdSchedulerFactory.getDefaultScheduler();
        JobDetail testJob = JobBuilder.newJob(HelloJob.class)
                .withIdentity("testJob","group1")
                .build();

        Trigger testTrigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger","group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever())
                .build();

        scheduler.scheduleJob(testJob,testTrigger);
        scheduler.start();
    }
}
