package com.ad.admain.service.impl;

import com.ad.admain.AdLaunchApplication;
import com.ad.admain.repository.OrderReposity;
import com.ad.admain.to.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {
@Autowired
    OrderReposity orderReposity;
    @Test
    public void save() {
        Order order = new Order()
                .setId(2)
                ;
        Order order1 = orderReposity.save(order);
        System.out.println(Optional.ofNullable(order1).toString());
    }
}