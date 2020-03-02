package com.ad.admain.mq.order;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
public interface PaymentOrderProduceI {

    void paymentOrder(final PaymentOrderMessage orderMessage);
}
