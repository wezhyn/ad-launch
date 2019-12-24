package com.ad.admain.message.common;

/**
 * @author wezhyn
 * @date 2019/09/26
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public enum TargetTypeEnum {

    /**
     * targetType 发送消息时的类型
     */
    USERS("users"), CHAT_GROUPS("chatgroups"), CHAT_ROOMS("chatrooms");

    private String targetType;

    TargetTypeEnum(String targetType) {
        this.targetType=targetType;
    }


    @Override
    public String toString() {
        return targetType;
    }
}
