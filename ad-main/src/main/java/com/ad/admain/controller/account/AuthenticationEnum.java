package com.ad.admain.controller.account;

import com.wezhyn.project.BaseEnum;
import com.wezhyn.project.utils.EnumUtils;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public enum AuthenticationEnum implements BaseEnum {

    /**
     * show authenticate
     * ordinal: 权重
     * 用户的权限：包含当前权重之下的权限
     * 例如DEVELOPER 拥有 DEVELOPER,ADMIN,CUSTOMER,USER
     */
    USER(5, "user"),
    CUSTOMER(10, "customer"),
    PARTNER(12, "partner"),
    ADMIN(15, "admin"),
    DEVELOPER(20, "developer"),
    FINANCE(20, "finance");


    private int ordinal;
    private String value;

    AuthenticationEnum(int ordinal, String s) {
        this.ordinal=ordinal;
        this.value=s;
    }

    public static AuthenticationEnum valueOfRs(String roleName) {
        return EnumUtils.valueOfStringEnumIgnoreCase(AuthenticationEnum.class, roleName);
    }


    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }
}
