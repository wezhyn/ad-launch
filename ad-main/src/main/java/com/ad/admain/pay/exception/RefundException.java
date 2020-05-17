package com.ad.admain.pay.exception;

/**
 * @author wezhyn
 * @since 02.27.2020
 */
public class RefundException extends RuntimeException {
    public RefundException(String message) {
        super(message);
    }

    public RefundException(Throwable cause) {
        super(cause);
    }
}
