package com.ad.admain.exception;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class SaveOperationException extends RuntimeException {

    public SaveOperationException() {
    }

    public SaveOperationException(String message) {
        super(message);
    }

    public SaveOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
