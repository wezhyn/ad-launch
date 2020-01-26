package com.ad.admain.controller.quartz.service;

import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.quartz.entity.JobEntity;
import org.quartz.*;

import java.util.List;

public interface DynamicJobService {
     //通过Id获取Job
     JobEntity getJobEntityById(Integer id);

    //从数据库中加载获取到所有Job
     List<JobEntity> loadJobs();

    //获取JobDataMap.(Job参数对象)
     JobDataMap getJobDataMap(JobEntity job);

    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map);

    //获取Trigger (Job的触发器,执行规则)
     Trigger getTrigger(JobEntity job);

    //获取JobKey,包含Name和Group
     JobKey getJobKey(JobEntity job);

     void insertJobEntity(Order order);



}
