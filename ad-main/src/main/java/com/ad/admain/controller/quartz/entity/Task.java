package com.ad.admain.controller.quartz.entity;

import lombok.Builder;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobKey;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @ClassName Task
 * @Description 任务定义
 * @Author ZLB
 * @Date 2020/1/22 22:37
 * @Version 1.0
 */
@Data
@Builder
public class Task {
    /**
     * 定时任务的名字和分组 name,group {@link JobKey}
     */
    @NotNull(message = "Job的name 和 group不能为空")
    private JobKey jobKey;

    /**
     * 定时任务的描述(可为任务或者触发器)
     * {@link org.quartz.JobDetail} {@link org.quartz.Trigger}
     */
    private String description;

    /**
     * 定时任务执行的cron(Trigger 的 CronScheduleBuilder的cronExpression )
     *{@link org.quartz.Trigger} CronScheduleBuilder {@link org.quartz.CronScheduleBuilder}
     */
    @NotEmpty(message = "执行定时任务的cron表达式不能为空")
    private String cronExpression;

    /**
     * 定时任务的元数据
     * {@link org.quartz.JobDataMap}
     */
    private Map<?,?> jobDataMap;

    /**
     * 定时任务具体执行的逻辑类
     * {@link org.quartz.Job}
     */
    @NotNull(message = "定时任务的具体执行逻辑类，不能为空")
    private Class<? extends Job> jobClass;
}
