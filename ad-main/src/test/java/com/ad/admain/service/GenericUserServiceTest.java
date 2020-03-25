package com.ad.admain.service;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.launch.user.AuthenticationEnum;
import com.wezhyn.project.account.SexEnum;
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
        for (int i=0; i < 100; i++) {
            GenericUser genericUser=GenericUser.builder()
                    .username("wezhyn" + i)
                    .password(passwordEncoder.encode("wezhyn"))
                    .roles(AuthenticationEnum.CUSTOMER)
                    .enable(GenericUser.UserEnable.NORMAL)
                    .email("wezhyn" + i + "@test.com")
                    .sex(SexEnum.MALE)
                    .intro("test")
                    .build();
            genericUserService.save(genericUser);
        }
    }

    @Test
    public void getGenericUser() {
        Optional<GenericUser> genericUser=genericUserService.getUserByUsername("zhao");
        System.out.println(genericUser.get());
    }
}
