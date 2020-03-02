package com.ad.admain.mq.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
@Getter
@ToString
public class PaymentOrderMessage {

    public final static String TOPIC_TAG="order-topic:payment-order";

    private final int orderId;


    @JsonCreator
    public PaymentOrderMessage(int orderId) {
        this.orderId=orderId;

    }
}
