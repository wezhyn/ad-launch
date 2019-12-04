package com.ad.admain.service;

import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.to.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {
    @Autowired
    OrderService orderService;

    public void create(){
        orderService.save(new Order().setPrice(1000D));
    }
}