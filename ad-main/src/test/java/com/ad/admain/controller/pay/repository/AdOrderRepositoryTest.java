package com.ad.admain.controller.pay.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * @ClassName AdOrderRepositoryTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/18 12:10
 * @Version V1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdOrderRepositoryTest {
@Autowired
AdOrderRepository adOrderRepository;
    @Test
    public void updateExecuted() {
        Integer i = adOrderRepository.updateExecuted(102,100.0);
        assertEquals(Integer.valueOf(1),i);
    }
}