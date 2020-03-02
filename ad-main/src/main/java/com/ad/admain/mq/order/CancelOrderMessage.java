package com.ad.admain.mq.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * @author wezhyn
 * @since 02.27.2020
 */
@Getter
public class CancelOrderMessage {

    public static final String MESSAGE_TOPIC="order-topic:cancel-order";

    private final Integer orderId;

    private final Integer uid;


    @JsonCreator
    public CancelOrderMessage(@JsonProperty("orderId") Integer orderId,
                              @JsonProperty("uid") Integer uid) {
        Assert.notNull(orderId, "取消订单id不能为空");
        Assert.notNull(uid, "取消订单用户id不能为空");
        this.orderId=orderId;
        this.uid=uid;
    }

}
