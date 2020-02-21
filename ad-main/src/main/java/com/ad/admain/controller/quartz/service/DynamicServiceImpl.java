package com.ad.admain.controller.quartz.service;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.quartz.dao.JobEntityRepository;
import com.ad.admain.controller.quartz.entity.JobEntity;
import com.ad.admain.controller.quartz.job.DynamicJob;
import com.ad.admain.controller.quartz.job.InitializeJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName DynamicServiceImpl
 * @Description job接口实现
 * @Param
 * @Author ZLB
 * @Date 2020/1/26 11:28
 * @Version 1.0
 */
@Service
public class DynamicServiceImpl implements DynamicJobService{
    @Autowired
    private JobEntityRepository repository;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    OrderService orderService;

    @Override
    public JobEntity insertOneJob(JobEntity jobEntity) {
        return repository.save(jobEntity);
    }

    //通过Id获取Job
    @Override
    public JobEntity getJobEntityById(Integer id) {
        return repository.getById(id);
    }

    //从数据库中加载获取到所有Job
    @Override
    public List<JobEntity> loadJobs() {
        return repository.findAll();
    }

    //获取JobDataMap.(Job参数对象)
    @Override
    public JobDataMap getJobDataMap(JobEntity job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("jobGroup", job.getJobGroup());
        map.put("cronExpression", job.getCron());
        map.put("jobDescription", job.getDescription());
        map.put("status", job.getStatus());
        map.put("isFinished",job.getIsFinished());
        map.put("order_id",job.getOrder().getId());
        map.put("equip_id",job.getEquip().getId());
        map.put("amount",job.getAmount());
        map.put("job_id",job.getId());
        return map;
    }

    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
    @Override
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                //即使没有与trigger关联 也要将改job保留起来
//                .storeDurably(true)
                .requestRecovery(true)
                .build();
    }

    //获取Trigger (Job的触发器,执行规则)
    @Override
    public Trigger getTrigger(JobEntity job) {
        try {
            return TriggerBuilder.newTrigger()
                    .withIdentity(job.getName(), job.getJobGroup())
//                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                            .withIntervalInSeconds(300/StringUtils.carRate)
                            .withIntervalInSeconds(5)
                            .withRepeatCount(job.getAmount()))
                    .usingJobData(getJobDataMap(job))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return null;
    }

    //获取JobKey,包含Name和Group
    @Override
    public JobKey getJobKey(JobEntity job) {
        return JobKey.jobKey(job.getName(), job.getJobGroup());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateJobs(Order order) {
        Integer rate = order.getRate();
        Integer deliverNum = order.getDeliverNum();
        Integer num = order.getNum();
        Double lati = order.getLatitude();
        Double lgti = order.getLongitude();
        Double scope = order.getScope();
        //订单要求的车辆数大于范围内可用的车辆数或投放的广告总数不是投放车辆数的整数倍
        Long available = equipmentService.countAllAvailableEquips(true,rate,lgti,lati,scope);
        if (available<deliverNum||num%deliverNum!=0){
            return ;
        }

        int amount = num/deliverNum;
        List<Equipment> equipmentList = equipmentService.findAllAvailableEquips(true,rate,lgti,lati,scope);
        for (int i = 0; i < deliverNum; i++) {
            Equipment equipment  = equipmentList.get(i);
            JobEntity jobEntity = new JobEntity();
            jobEntity.setAmount(amount)
                    .setJobGroup(order.getId().toString())
                    .setName(order.getId().toString())
                    .setDescription("order_id:"+order.getId()+"-task_num:"+i)
                    .setStatus("CLOSE")
                    .setOrder(order)
                    .setEquip(equipment);
            equipment.setRemain(equipment.getRemain()-rate);
            equipmentService.save(equipment);
            repository.save(jobEntity);
        }
        }
        //获取job的初始化触发器
    @Override
    public Trigger getInitialTrigger() {
        try {
            return TriggerBuilder.newTrigger()
                    .withIdentity("initializeJob", "initializeJob")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                            .withIntervalInMinutes(5)
                            .withIntervalInSeconds(5)
                            .repeatForever()
                            )
                    .startNow()
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;    }

        //获取初始化job的JobDetail
    @Override
    public JobDetail getInitialJobDetail() {
        String des = "to load job every 5 mins";
        JobDetail jobDetail = JobBuilder.newJob(InitializeJob.class)
                .withIdentity("initializeJob","initializeJob")
                .withDescription(des)
                .storeDurably(true)
                .requestRecovery(true)
                .build();
        return jobDetail;
    }

    @Override
    public List<JobEntity> loadJobsByStatus(String status) {
        return repository.findAllByStatusEquals(status);
    }

    @Override
    public JobEntity updateJobEntity(JobEntity jobEntity) {
        return repository.save(jobEntity);
    }


}
