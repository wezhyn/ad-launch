package com.ad.admain.controller.pay.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 02.27.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdRemoteOrderRepositoryTest {


    @Autowired
    private AdOrderRepository orderRepository;

    @Test
    public void findAdOrdersByUid() {
        System.out.println(orderRepository.existsByIdAndUid(90, 1));
    }
}