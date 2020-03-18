package com.ad.screen.server.exception;

/**
 * @ClassName Insufficient
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/18 21:01
 * @Version V1.0
 **/
public class InsufficientException extends RuntimeException {
    public InsufficientException() {
    }

    public InsufficientException(String message) {
        super(message);
    }

    public InsufficientException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientException(Throwable cause) {
        super(cause);
    }

    public InsufficientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
