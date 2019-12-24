package com.wezhyn.project.exception;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class DeleteOperateException extends RuntimeException {

    public DeleteOperateException() {
    }

    public DeleteOperateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteOperateException(String message) {
        super(message);
    }
}
