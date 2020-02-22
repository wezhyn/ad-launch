package com.ad.admain.controller.impl;

import com.ad.admain.config.QqCloudSmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QCloudSmsServiceTest {


    @Autowired
    private QqCloudSmsProperties properties;

    @Test
    public void sendSms() {
        System.out.println(properties);
    }
}