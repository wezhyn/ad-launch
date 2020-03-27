package com.ad.admain.mq.order;

import com.ad.launch.order.TaskMessage;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
public interface PaymentOrderProduceI {

    void paymentOrder(final TaskMessage orderMessage);
}
