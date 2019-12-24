package com.ad.admain.controller.account;

import com.wezhyn.project.BaseEnum;
import com.wezhyn.project.StringEnum;
import com.wezhyn.project.utils.EnumUtils;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * 性别枚举类
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public enum SexEnum implements StringEnum, BaseEnum {

    /**
     * 性别 1：男 2：女 0:未提供
     */
    MALE(1, "male"), FEMALE(2, "female"),
    UNKNOWN(0, "unknown");

    private int ordinal;
    private String value;

    SexEnum(int ordinal, String value) {
        this.ordinal=ordinal;
        this.value=value;
    }

    public static SexEnum toSexEnum(String value) {
        return EnumUtils.valueOfStringEnumIgnoreCase(SexEnum.class, value);
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String getValue() {
        return value;
    }

}
