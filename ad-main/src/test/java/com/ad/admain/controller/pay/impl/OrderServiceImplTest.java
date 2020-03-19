package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.to.AdOrder;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @ClassName OrderServiceImplTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/20 0:08
 * @Version V1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
@Autowired
    AdOrderService adOrderService;
    @Test
    public void findByEnum() {
        List<AdOrder> adOrders = adOrderService.findByEnum(3);
        System.out.println(adOrders.toString());
    }
}