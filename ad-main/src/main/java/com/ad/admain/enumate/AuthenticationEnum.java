package com.ad.admain.enumate;

import com.ad.admain.utils.EnumUtils;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public enum AuthenticationEnum implements BaseEnum {

    /**
     * show authenticate
     */
    USER(1, "user"),
    ADMIN(2, "admin");

    private int ordinal;
    private String value;

     AuthenticationEnum(int ordinal, String s) {
        this.ordinal=ordinal;
        this.value=s;
    }

    public static AuthenticationEnum valueOfRs(String roleName) {
        return EnumUtils.valueOfBaseEnum(AuthenticationEnum.class, roleName);
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
