package com.ad.admain.enumate;

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
    GUIDE("guide"), SHUFFING("shuffing"), AVATAR("avatar");

    private String value;

    ImgBedType(String value) {
        this.value=value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
