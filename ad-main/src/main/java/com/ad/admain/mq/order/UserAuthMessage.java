package com.ad.admain.mq.order;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Getter
@Setter
public class UserAuthMessage {
    public static final String MESSAGE_TOPIC = "order-topic:auth-order";

    private static final String className = UserAuthMessage.class.getName();
    private Integer orderId;
    private Integer uid;

    public UserAuthMessage(Integer orderId, Integer uid) {
        this.orderId = orderId;
        this.uid = uid;
    }

    public UserAuthMessage() {
    }
}
