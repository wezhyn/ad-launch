package com.ad.screen.client;

import com.ad.launch.order.TaskMessage;
import com.google.gson.Gson;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Collections;

import static com.ad.screen.client.ScreenClient.createMq;

/**
 * @author wezhyn
 * @since 05.05.2020
 */
public class SingleMessageClient {
    public static void main(String[] args) {
        DefaultMQProducer producer = createMq();
        int rate = 5;
        int dn = 2;
        int orderNum = 20;
        TaskMessage taskMessage = TaskMessage.builder()
                .deliverNum(dn)
                .latitude(30.0)
                .longitude(120.0)
                .uid(orderNum % 1000)
                .oid(orderNum)
                .produceContext(Collections.singletonList("测试-测试-测试-测试-测试-测试-测试-" + orderNum))
                .rate(rate)
                .vertical(true)
                .scope(Double.MAX_VALUE)
                .totalNum(dn * rate * 2)
                .build();
        Gson gson = new Gson();
        Message message = new Message("task_message_topic", "*", gson.toJson(taskMessage).getBytes());
        try {
            producer.send(message);
        } catch (MQClientException | RemotingException | InterruptedException | MQBrokerException e) {
            e.printStackTrace();
        }

    }
}
