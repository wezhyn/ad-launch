package com.ad.admain.controller.quartz.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @ClassName HelloJob
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/19 22:20
 * @Version 1.0
 */
public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("fuck you");
    }
}
