package com.ad.admain.service.impl;

import com.ad.admain.controller.pay.repository.OrderReposity;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.OrderVerify;
import com.ad.admain.controller.pay.to.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {
    @Autowired
    OrderReposity orderReposity;


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
                .setStartTime(new Date(System.currentTimeMillis()));
        Order order1=orderReposity.save(order);
        System.out.println(Optional.ofNullable(order1).toString());
    }


}