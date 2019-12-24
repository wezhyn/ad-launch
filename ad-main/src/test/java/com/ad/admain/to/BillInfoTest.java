package com.ad.admain.to;

import com.ad.admain.controller.pay.OrderInfoService;
import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.to.BillInfo;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BillInfoTest {


    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderInfoService orderInfoService;

    @Test
    public void saveOrderInfo() {
        Value value=new Value("test");
        Order order=new Order()
                .setPrice(100D)
                .setValueList(Collections.singletonList(value))
                .setRate(5);
        order=orderService.save(order).get();
        BillInfo orderInfo=BillInfo.builder()
                .orderId(order.getId())
                .build();
        orderInfoService.save(orderInfo);
    }
}