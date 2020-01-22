package com.ad.admain.controller.quartz.execute;

import com.ad.admain.controller.equipment.EquipmentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName AssignmentJob
 * @Description 设备搜索任务
 * @Param
 * @Author ZLB
 * @Date 2020/1/21 22:29
 * @Version 1.0
 */
public class EquipSearchJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("quartz test");
    }

}
