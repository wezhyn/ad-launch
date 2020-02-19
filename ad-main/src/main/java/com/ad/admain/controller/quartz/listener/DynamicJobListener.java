package com.ad.admain.controller.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @ClassName JobListener
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/27 18:07
 * @Version 1.0
 */
public class DynamicJobListener implements org.quartz.JobListener {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

    }
}
