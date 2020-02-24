package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.to.AdOrder;
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
public class AdBillInfoServiceImplTest {

    @Autowired
    private AdOrderService orderService;


    @Test
    public void getOrder() {
        Optional<AdOrder> byId=orderService.getById(15);
        System.out.println(byId.isPresent());
    }
}