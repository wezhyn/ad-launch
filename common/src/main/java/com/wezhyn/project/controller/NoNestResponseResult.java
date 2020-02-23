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


    /**
     * 成功：20000
     */
    private int code;
    private T data;
    private String message;


    private NoNestResponseResult(ResponseType type, T data, String message) {
        this.code=type.getNumber();
        this.data=data;
        this.message=message;
    }

    public static <T> NoNestResponseResult<T> successResponseResult(String message, T data) {
        return new NoNestResponseResult<>(ResponseType.SUCCESS, data, message);
    }

    /**
     * 用于无登录信息的返回
     *
     * @param message 信息
     * @return 报错信息
     */
    public static NoNestResponseResult<Void> failureLoginResponseResult(String message) {
        return new NoNestResponseResult<>(ResponseType.NO_LOGIN_AUTHENTICATION, null, message);
    }

    public static <T> NoNestResponseResult<T> failureResponseResult(String message) {
        return new NoNestResponseResult<>(ResponseType.GENERIC_FAIL, null, message);
    }

}
