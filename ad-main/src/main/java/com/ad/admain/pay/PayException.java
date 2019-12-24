package com.ad.admain.pay;

/**
 * 支付的错误
 *
 * @author wezhyn
 * @since 11.22.2019
 */
public class PayException extends RuntimeException {


    public PayException() {
    }

    public PayException(String message) {
        super(message);
    }

    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(Throwable cause) {
        super(cause);
    }
}
