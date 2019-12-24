package com.wezhyn.project;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface BaseEnum extends StringEnum, NumberEnum {
    /**
     * 返回当前序列
     *
     * @return int
     */
    int getOrdinal();

    @Override
    default Integer getNumber() {
        return getOrdinal();
    }
}
