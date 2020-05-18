package com.ad.launch.order;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
@Data
public class CompleteTaskMessage {

    public static final String MESSAGE_TOPIC = "order-topic:completed-order";

    private Integer orderId;

    /**
     * 订单支付给车主的费用
     */
    private Double orderCost;

    private String endTime;

    public CompleteTaskMessage(Integer orderId, Double orderCost) {
        this.orderId = orderId;
        this.orderCost = orderCost;
        this.endTime = LocalDateTime.now().toString();
    }

    public CompleteTaskMessage() {
    }
}
