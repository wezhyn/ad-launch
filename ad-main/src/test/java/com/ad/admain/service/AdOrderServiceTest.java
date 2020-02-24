package com.ad.admain.service;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.to.AdOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdOrderServiceTest {
    @Autowired
    AdOrderService orderService;


    public void create() {
        AdOrder order=new AdOrder();
        order.setPrice(1000D);
        orderService.save(order);
    }
}