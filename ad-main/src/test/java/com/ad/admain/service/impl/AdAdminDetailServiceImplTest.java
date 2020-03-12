package com.ad.admain.service.impl;

import com.ad.admain.controller.account.administrator.Admin;
import com.ad.admain.controller.account.administrator.AdminService;
import com.ad.launch.user.AuthenticationEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdAdminDetailServiceImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminService adminService;


    @Test
    public void create() {
        Admin admin=Admin.newBuilder()
                .username("zhaoo")
                .password(passwordEncoder.encode("zhaoo"))
                .roles(AuthenticationEnum.DEVELOPER)
                .nickName("兆儿子")
                .email("zhaoo@vip.com")
                .build();
        adminService.save(admin);

    }

    @Test
    public void encode() {
        String str=passwordEncoder.encode("zhaoo");
        System.out.println(str);
    }
}