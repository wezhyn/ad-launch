package com.ad.admain.controller.quartz.service;

import com.ad.admain.controller.quartz.dao.JobEntityRepository;
import com.ad.admain.controller.quartz.entity.JobEntity;
import com.ad.admain.controller.quartz.job.DynamicJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName DynamicServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/26 11:28
 * @Version 1.0
 */
@Service
public class DynamicServiceImpl implements DynamicJobService{
    @Autowired
    private JobEntityRepository repository;

    //通过Id获取Job
    public JobEntity getJobEntityById(Integer id) {
        return repository.getById(id);
    }

    //从数据库中加载获取到所有Job
    public List<JobEntity> loadJobs() {
        return repository.findAll();
    }

    //获取JobDataMap.(Job参数对象)
    public JobDataMap getJobDataMap(JobEntity job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("jobGroup", job.getJobGroup());
        map.put("cronExpression", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("jobDescription", job.getDescription());
        map.put("vmParam", job.getVmParam());
        map.put("jarPath", job.getJarPath());
        map.put("status", job.getStatus());
        return map;
    }

    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                //即使没有与trigger关联 也要将改job保留起来
                .storeDurably()
                .build();
    }

    //获取Trigger (Job的触发器,执行规则)
    public Trigger getTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getName(), job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    //获取JobKey,包含Name和Group
    public JobKey getJobKey(JobEntity job) {
        return JobKey.jobKey(job.getName(), job.getJobGroup());
    }
}
