package com.ad.admain.controller.pay.exception;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
public class OrderStatusException extends RuntimeException {

    public OrderStatusException() {
    }

    public OrderStatusException(String message) {
        super(message);
    }
}
