package com.wezhyn.project;

/**
 * 定义枚举的转变策略：String -> Enum; Enum -> String
 *
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface StringEnum {
    /**
     * 枚举类对应的值
     *
     * @return value
     */
    String getValue();
}
