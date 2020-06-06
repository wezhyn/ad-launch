package com.ad.screen.server.entity;

import com.ad.screen.server.server.ScreenChannelInitializer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
@Getter
@Slf4j
public class FixedTask {

    public final static int EQUIP_MAX_ENTRY_ID = 1000;

    /**
     * 父任务
     */
    private Task task;
    private int repeatNum;
    private int equipEntryId;

    private static final int MAX_RETRY = 2;

    /**
     * 当前数据帧尝试数次
     */
    private int retryTime = 0;


    public FixedTask(Task task) {
        this.task = task;
        repeatNum = task.repeatReduce(task.getRate());
        int routeNum = task.getRepeatRoute() * ScreenChannelInitializer.SCHEDULE_NUM;
        equipEntryId = routeNum + task.getEntryId();
    }

    public boolean isSendAgain() {
        final int retry = getAndInrRetry();
        return retry > MAX_RETRY;
    }

    public int getAndInrRetry() {
        retryTime += 1;
        return retryTime;
    }

    public void resetRetry() {
        retryTime = 0;
    }


    public int getRepeatNum() {
        return repeatNum;
    }
}
