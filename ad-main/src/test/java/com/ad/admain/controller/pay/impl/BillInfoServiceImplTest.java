package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.to.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.26.2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BillInfoServiceImplTest {

    @Autowired
    private OrderService orderService;


    @Test
    public void getOrder() {
        Optional<Order> byId=orderService.getById(15);
        System.out.println(byId.isPresent());
    }
}