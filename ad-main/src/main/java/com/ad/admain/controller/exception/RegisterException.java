package com.ad.admain.controller.exception;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class RegisterException extends RuntimeException {

    public RegisterException() {
    }

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
