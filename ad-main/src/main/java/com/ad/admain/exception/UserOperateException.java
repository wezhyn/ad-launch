package com.ad.admain.exception;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class UserOperateException extends RuntimeException {

    public UserOperateException() {
    }

    public UserOperateException(String message) {
        super(message);
    }

    public UserOperateException(String message, Throwable cause) {
        super(message, cause);
    }
}
