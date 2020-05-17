package com.ad.screen.server.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EquipTaskRepositoryTest {

    @Autowired
    private EquipTaskRepository equipTaskRepository;

    @Test
    public void tryTaskComplete() {
        System.out.println(equipTaskRepository.tryTaskComplete("10.32.128.2", 22));
    }
}