package com.ad.admain.controller.pay.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 01.01.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BillInfoRepositoryTest {

    @Autowired
    private BillInfoRepository billInfoRepository;


    @Test
    public void get() {
        System.out.println(billInfoRepository.findById(7));
    }
}