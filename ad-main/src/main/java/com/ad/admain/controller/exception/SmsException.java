package com.ad.admain.controller.exception;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class SmsException extends Exception {


    public SmsException(String message) {
        super(message);
    }

    public SmsException(Throwable cause) {
        super(cause);
    }
}
