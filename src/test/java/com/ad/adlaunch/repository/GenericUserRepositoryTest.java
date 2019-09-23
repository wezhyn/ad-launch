package com.ad.adlaunch.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author : wezhyn
 * @date : 2019/09/23
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericUserRepositoryTest {

    @Autowired
    private GenericUserRepository genericUserRepository;

    @Test
    public void updateUserAvatar() {
        genericUserRepository.updateUserAvatar("wezhyn", "11");
    }
}