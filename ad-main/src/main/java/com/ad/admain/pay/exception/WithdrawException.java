package com.ad.admain.pay.exception;

/**
 * 提现异常
 *
 * @author wezhyn
 * @since 02.24.2020
 */
public class WithdrawException extends RuntimeException {

    public WithdrawException(String message) {
        super(message);
    }

    public WithdrawException(String message, Throwable cause) {
        super(message, cause);
    }

    public WithdrawException(Throwable cause) {
        super(cause);
    }
}
