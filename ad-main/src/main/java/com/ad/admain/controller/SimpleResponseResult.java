package com.ad.admain.controller;

import lombok.Data;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
public class SimpleResponseResult<T> {

    public static final int SUCCESS_CODE=20000;
    public static final int FAILURE_CODE=50000;
    /**
     * 成功：20000
     */
    private int code;
    private T data;
    private String message;

    private SimpleResponseResult(int code, T data, String message) {
        this.code=code;
        this.data=data;
        this.message=message;
    }

    public static <T> SimpleResponseResult<T> successResponseResult(String message, T data) {
        return new SimpleResponseResult<>(SUCCESS_CODE, data, message);
    }

    public static <T> SimpleResponseResult<T> failureResponseResult(String message) {
        return new SimpleResponseResult<>(FAILURE_CODE, null, message);
    }

}
