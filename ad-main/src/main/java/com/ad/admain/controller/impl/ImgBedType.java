package com.ad.admain.controller.impl;

import com.wezhyn.project.StringEnum;

/**
 * @author wezhyn
 * @date 2019/09/29
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public enum ImgBedType implements StringEnum {

    /**
     * 图床类型
     */
    GUIDE("guide"), SHUFFING("shuffing"), AVATAR("avatar"), ID_CARD("card");

    private String value;

    ImgBedType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
