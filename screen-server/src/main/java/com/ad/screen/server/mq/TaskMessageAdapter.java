package com.ad.screen.server.mq;

import com.ad.launch.order.TaskMessage;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
public class TaskMessageAdapter implements PrepareTaskMessage {

    private TaskMessage taskMessage;

    public TaskMessageAdapter(TaskMessage taskMessage) {
        this.taskMessage=taskMessage;
    }

    public Integer getTotalNum() {
        return taskMessage.getTotalNum();
    }

    public Integer getNumPerEquip() {
        return taskMessage.getNumPerEquip();
    }

    @Override
    public Integer getDeliverNum() {
        return taskMessage.getDeliverNum();
    }

    @Override
    public Integer getRate() {
        return taskMessage.getRate();
    }

    @Override
    public Double getLongitude() {
        return taskMessage.getLongitude();
    }

    @Override
    public Double getLatitude() {
        return taskMessage.getLatitude();
    }

    @Override
    public Double getScope() {
        return taskMessage.getScope();
    }
}
