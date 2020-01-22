package com.ad.admain.controller.quartz.test.job;

import lombok.NoArgsConstructor;
import org.quartz.*;

/**
 * @ClassName HelloJob
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/19 22:20
 * @Version 1.0
 */
public class HelloJob implements Job {
    public HelloJob(){

    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("this is a test");
    }
}
