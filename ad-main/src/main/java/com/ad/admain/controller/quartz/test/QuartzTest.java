package com.ad.admain.controller.quartz.test;

import com.ad.admain.controller.assignment.entity.Assignment;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
//静态导入DSL
import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;
/**
 * @ClassName QuartzTest
 * @Description quartz测试文件
 * @Param
 * @Author ZLB
 * @Date 2020/1/19 17:45
 * @Version 1.0
 */
public class QuartzTest {
    public static void main(String[] args) throws SchedulerException {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            //do something
            //只有当scheduler被启动后并且处于非stand-by状态下trigger才会被触发

            //job或trigg都有一个name和group组成
            //将helloJob与job进行绑定
            JobDetail job = newJob(HelloJob.class)
                    .withIdentity("myJob","group1")
                    .build();
            //马上触发job,并且每隔40秒执行一次
            Trigger trigger = newTrigger()
                    .withIdentity("myTrigger","group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder
                    .simpleSchedule()
                    .withIntervalInSeconds(40)
                    .repeatForever()
                    ).build();
            scheduler.scheduleJob(job,trigger);
            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
