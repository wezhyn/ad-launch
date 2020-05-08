package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.convert.AdOrderWithUserMapper;
import com.ad.admain.controller.pay.to.AdOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
    @Autowired
    private AdOrderWithUserMapper orderWithUserMapper;

    @Test
    public void findByEnum() {
        List<AdOrder> adOrders = adOrderService.findByEnum(3);
        System.out.println(adOrders.toString());
    }

    @Test
    public void listOrdersWithUsers() {
        final Page<AdOrder> orders = adOrderService.listOrdersWithUsername(10, 1);
        System.out.println(orderWithUserMapper.toDtoList(orders.getContent()));
        System.out.println(orders.getContent());
    }
}