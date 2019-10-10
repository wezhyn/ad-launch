package com.ad.admain.service;

import com.ad.admain.to.GenericUser;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericUserServiceTest {

    @Autowired
    private GenericUserService genericUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void saveGenericUser() {
        GenericUser genericUser=GenericUser.builder()
                .username("wezhyn1")
                .password(passwordEncoder.encode("zhaoo"))
                .roles(AuthenticationEnum.CUSTOMER)
                .email("zhaoo@zhaoo.com")
                .sex(SexEnum.MALE)
                .build();
        genericUserService.save(genericUser);
    }

    @Test
    public void getGenericUser() {
        Optional<GenericUser> genericUser=genericUserService.getById("zhaoo");
        System.out.println(genericUser.get());
    }
}
