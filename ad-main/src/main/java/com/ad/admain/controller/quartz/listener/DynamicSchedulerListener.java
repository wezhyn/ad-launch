package com.ad.admain.controller.quartz.listener;

import com.ad.admain.controller.quartz.dao.JobEntityRepository;
import com.ad.admain.controller.quartz.entity.JobEntity;
import com.ad.admain.controller.quartz.service.DynamicJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @ClassName DynamicSchedulerListener
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/27 18:11
 * @Version 1.0
 */
@Component
@Slf4j
public class DynamicSchedulerListener implements SchedulerListener {
    @Autowired

    JobEntityRepository jobEntityRepository;
//    DynamicJobService dynamicJobService;
    @Override
    public void jobScheduled(Trigger trigger) {

    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {

    }

    @Transactional
    @Override
    public void triggerFinalized(Trigger trigger) {
        JobDataMap jobDataMap = trigger.getJobDataMap();
        int jobId = jobDataMap.getInt("job_id");
        log.info(""+jobId);
//        JobEntity jobEntity = dynamicJobService.getJobEntityById(jobId);
        JobEntity jobEntity = jobEntityRepository.findById(jobId).orElse(null);
        jobEntity.setIsFinished(true);
        jobEntityRepository.save(jobEntity);
        log.info("job_id:"+jobId+"已完成");
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {

    }

    @Override
    public void triggersPaused(String triggerGroup) {

    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {

    }

    @Override
    public void triggersResumed(String triggerGroup) {

    }

    @Override
    public void jobAdded(JobDetail jobDetail) {

    }

    @Override
    public void jobDeleted(JobKey jobKey) {

    }

    @Override
    public void jobPaused(JobKey jobKey) {

    }

    @Override
    public void jobsPaused(String jobGroup) {

    }

    @Override
    public void jobResumed(JobKey jobKey) {

    }

    @Override
    public void jobsResumed(String jobGroup) {

    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {

    }

    @Override
    public void schedulerInStandbyMode() {

    }

    @Override
    public void schedulerStarted() {

    }

    @Override
    public void schedulerStarting() {

    }

    @Override
    public void schedulerShutdown() {

    }

    @Override
    public void schedulerShuttingdown() {

    }

    @Override
    public void schedulingDataCleared() {

    }
}
