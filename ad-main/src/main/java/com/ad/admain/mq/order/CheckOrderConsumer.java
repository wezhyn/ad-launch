package com.ad.admain.mq.order;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.launch.order.TaskMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
@RocketMQMessageListener(topic="order-topic", consumerGroup="cancel-order-group",
        selectorExpression="cancel-order")
@Component
public class CheckOrderConsumer implements RocketMQListener<CheckOrderMessage> {

    @Autowired
    private AdOrderService orderService;
    @Autowired
    private PaymentOrderProduceI paymentOrderProduce;


    @Override
    public void onMessage(CheckOrderMessage payload) {
        final Optional<AdOrder> status=orderService.trySuccessOrder(payload.getOrderId(), payload.getUid());
        status.map(o->{
            paymentOrderProduce.paymentOrder(createTask(o));
            return null;
        });
    }


    private TaskMessage createTask(AdOrder order) {
        return TaskMessage.builder()
                .deliverNum(order.getDeliverNum())
                .latitude(order.getLatitude())
                .longitude(order.getLongitude())
                .numPerEquip(order.getNumPerEquip())
                .oid(order.getId())
                .produceContext(order.getProduceContext())
                .rate(order.getRate())
                .scope(order.getScope())
                .totalNum(order.getNum())
                .vertical(order.getProduce().getVertical())
                .uid(order.getUid())
                .build();

    }
}
