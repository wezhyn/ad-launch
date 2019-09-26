package com.ad.message.api.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author wezhyn
 * @date 2019/09/25:20:37
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class EasemobAuthTokenTest {

    @Test
    public void getAuthToken() {
        System.out.println(new EasemobAuthToken().getAuthToken());
    }
}