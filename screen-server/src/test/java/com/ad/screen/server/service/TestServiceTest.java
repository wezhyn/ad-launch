package com.ad.screen.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestServiceTest {


    @Autowired
    private TestService testService;

    @Test
    public void save() {
        testService.save();
    }
}