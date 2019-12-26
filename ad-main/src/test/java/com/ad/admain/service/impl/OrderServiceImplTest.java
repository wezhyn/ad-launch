package com.ad.admain.service.impl;

import com.ad.admain.controller.pay.OrderSearchType;
import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.repository.OrderReposity;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.OrderVerify;
import com.ad.admain.controller.pay.to.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {
    @Autowired
    OrderReposity orderReposity;
    @Autowired
    private OrderService orderService;

    @Test
    public void getList() {
        orderService.getList(PageRequest.of(0, 10))
                .forEach(System.out::println);
    }

    @Test
    public void get() {
        Order order=orderReposity.findById(4).get();
        System.out.println(order);
    }

    @Test
    public void save() {
        List<Value> valueList=new ArrayList<>();
        valueList.add(new Value("1232"));
        Order order=new Order()
                .setVerify(OrderVerify.WAIT_VERITY)
                .setUid(1)
                .setValueList(valueList)
                .setStartTime(LocalDateTime.now());
        Order order1=orderReposity.save(order);
        System.out.println(Optional.ofNullable(order1).toString());
    }

    @Test
    public void searchUser() {
        orderService.search(OrderSearchType.USER, "1", PageRequest.of(0, 10))
                .forEach(System.out::println);
    }
}