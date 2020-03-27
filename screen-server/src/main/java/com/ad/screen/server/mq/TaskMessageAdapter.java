package com.ad.screen.server.mq;

import com.ad.screen.server.entity.EquipTask;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
public class TaskMessageAdapter implements PrepareTaskMessage {

    private EquipTask taskMessage;

    public TaskMessageAdapter(EquipTask taskMessage) {
        this.taskMessage=taskMessage;
    }

    @Override
    public Integer getAvailableAllocateNum() {
        return taskMessage.getTotalNum();
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
