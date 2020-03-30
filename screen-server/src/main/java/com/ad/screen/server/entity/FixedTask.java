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

    public final static int EQUIP_MAX_ENTRY_ID=1000;

    /**
     * 父任务
     */
    private Task task;
    private int repeatNum;
    private int equipEntryId;

    public FixedTask(Task task) {
        this.task=task;
        repeatNum=task.repeatReduce(task.getRate());
        int routeNum=task.getRepeatRoute()*ScreenChannelInitializer.SCHEDULE_NUM;
        equipEntryId=routeNum + task.getEntryId();
    }


    public int getRepeatNum() {
        return repeatNum;
    }
}
