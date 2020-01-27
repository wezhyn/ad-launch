package com.ad.admain.controller.quartz.job;

import com.wezhyn.project.utils.HardWareUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName DynamicJob
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/26 11:25
 * @Version 1.0
 */

@DisallowConcurrentExecution
@Component
@Slf4j
public class DynamicJob implements Job {

    /**
     * 核心方法,Quartz Job真正的执行逻辑.
     *
     * @param executorContext executorContext JobExecutionContext中封装有Quartz运行所需要的所有信息
     * @throws JobExecutionException execute()方法只允许抛出JobExecutionException异常
     */
    @Override
    public void execute(JobExecutionContext executorContext) throws JobExecutionException {
        //JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
        JobDataMap map = executorContext.getMergedJobDataMap();
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        log.info("Running Job name : {} ", map.getString("name"));
        log.info("Running Job description : {}", map.getString("jobDescription"));
        log.info("Running Job group: {} ", map.getString("group"));
        log.info("分配到的订单号:"+map.getInt("order_id"));
        log.info("分配到的设备号"+map.getInt("equip_id"));
        log.info("任务的执行次数:"+map.getInt("amount"));
        HardWareUtils.distribute();
//        log.info(String.format("Running Job cron : %s", map.getString("cronExpression")));
//        log.info("Running Job parameter : {} ", parameter);
//        log.info("Running Job vmParam : {} ", vmParam);
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        log.info(">>>>>>>>>>>>> Running Job has been completed , cost time : {}ms\n ", (endTime - startTime));
    }

    //记录Job执行内容
    private void logProcess(InputStream inputStream, InputStream errorStream) throws IOException {
        String inputLine;
        String errorLine;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        while (Objects.nonNull(inputLine = inputReader.readLine())) log.info(inputLine);
        while (Objects.nonNull(errorLine = errorReader.readLine())) log.error(errorLine);
    }
}
