package com.ad.admain.mq.order;

import com.ad.launch.order.TaskMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * @author wezhyn
 * @since 05.08.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CheckOrderProduceImplTest {

    @Autowired
    private CheckOrderProduceImpl checkOrderProduce;

    @Test
    public void paymentOrder() {
        final TaskMessage message = TaskMessage.builder().uid(1).vertical(false).totalNum(12).scope(5D).rate(2)
                .produceContext(Collections.singletonList("test")).oid(8)
                .deliverNum(1)
                .longitude(120D)
                .latitude(30D)
                .build();
        checkOrderProduce.paymentOrder(message);
    }
}