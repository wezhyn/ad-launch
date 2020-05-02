package com.ad.admain.service.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.OrderSearchType;
import com.ad.admain.controller.pay.repository.AdOrderRepository;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdRemoteOrderServiceImplTest {
    @Autowired
    AdOrderRepository orderReposity;
    @Autowired
    private AdOrderService orderService;

    @Test
    public void getList() {
        orderService.getList(PageRequest.of(0, 10))
                .forEach(System.out::println);
    }

    @Test
    public void get() {
        Order order = orderReposity.findById(3).get();
        System.out.println(order);
    }


    @Test
    public void modifyStatus() {

        try {
            orderService.modifyOrderStatus(75, OrderStatus.SUCCESS_PAYMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            orderService.modifyOrderStatus(75, OrderStatus.REFUNDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            orderService.modifyOrderStatus(75, OrderStatus.CANCEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            orderService.modifyOrderStatus(75, OrderStatus.REFUNDING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void searchUser() {
        orderService.search(OrderSearchType.USER, "1", PageRequest.of(0, 10))
                .forEach(System.out::println);
    }
}