package com.ad.admain.mq.order;

import com.ad.launch.order.AdOrder;
import com.ad.launch.order.AdProduce;
import com.google.gson.Gson;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import java.util.Collections;

/**
 * @author wezhyn
 * @since 03.14.2020
 */
public class CancelConsumerTest {

    @Test
    public void onMessage() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer=new DefaultMQProducer("test-group");
        producer.setNamesrvAddr("47.111.185.61:9876");
        producer.start();
        AdProduce produce=AdProduce.builder()
                .produceContext(Collections.singletonList("123"))
                .deliverNum(1)
                .price(1D)
                .build();
        AdOrder order=new AdOrder();
        order.setProduce(produce);
        Gson gson=new Gson();
        final String json=gson.toJson(order);
        final SendResult send=producer.send(new Message("test-topic", "test", json.getBytes()));
        System.out.println(send.getSendStatus());
    }
}