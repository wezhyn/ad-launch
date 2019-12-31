package com.wezhyn.project.controller;

import lombok.Data;

/**
 * 不嵌套的数据
 *
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
public class NoNestResponseResult<T> {

    public static final int SUCCESS_CODE=20000;
    public static final int FAILURE_CODE=50000;
    /**
     * 成功：20000
     */
    private int code;
    private T data;
    private String message;

    private NoNestResponseResult(int code, T data, String message) {
        this.code=code;
        this.data=data;
        this.message=message;
    }

    public static <T> NoNestResponseResult<T> successResponseResult(String message, T data) {
        return new NoNestResponseResult<>(SUCCESS_CODE, data, message);
    }

    public static <T> NoNestResponseResult<T> failureResponseResult(String message) {
        return new NoNestResponseResult<>(FAILURE_CODE, null, message);
    }

}
