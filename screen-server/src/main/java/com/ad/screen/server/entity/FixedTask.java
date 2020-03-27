package com.ad.screen.server.entity;

import lombok.Getter;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
@Getter
public class FixedTask {

    public final static int FIX_REPEAT_NUM=5;

    /**
     * 父任务
     */
    private Task task;
    private int repeatNum;

    public FixedTask(Task task) {
        this.task=task;
        repeatNum=task.repeatReduce(FIX_REPEAT_NUM);
    }


    public int getRepeatNum() {
        return repeatNum;
    }
}
