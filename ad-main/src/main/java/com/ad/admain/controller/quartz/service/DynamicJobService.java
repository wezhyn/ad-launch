package com.ad.admain.controller.quartz.service;

import com.ad.admain.controller.quartz.entity.JobEntity;
import org.quartz.*;

import java.util.List;

public interface DynamicJobService {
    //通过Id获取Job
    public JobEntity getJobEntityById(Integer id);

    //从数据库中加载获取到所有Job
    public List<JobEntity> loadJobs();

    //获取JobDataMap.(Job参数对象)
    public JobDataMap getJobDataMap(JobEntity job);

    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map);

    //获取Trigger (Job的触发器,执行规则)
    public Trigger getTrigger(JobEntity job);

    //获取JobKey,包含Name和Group
    public JobKey getJobKey(JobEntity job);

}
