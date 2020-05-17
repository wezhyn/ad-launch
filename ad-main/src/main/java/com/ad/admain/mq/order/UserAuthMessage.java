package com.ad.admain.mq.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Getter
public class UserAuthMessage {
    public static final String MESSAGE_TOPIC = "order-topic:auth-order";

    private final Integer orderId;

    private final Integer uid;


    @JsonCreator
    public UserAuthMessage(@JsonProperty("orderId") Integer orderId,
                           @JsonProperty("uid") Integer uid) {
        Assert.notNull(orderId, "取消订单id不能为空");
        Assert.notNull(uid, "取消订单用户id不能为空");
        this.orderId = orderId;
        this.uid = uid;
    }

}
