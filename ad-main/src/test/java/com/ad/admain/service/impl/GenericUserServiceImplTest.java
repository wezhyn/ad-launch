package com.ad.admain.service.impl;

import com.ad.admain.controller.account.GenericUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/*
 *
 * @author ZLB_KAM
 * @date 2019/9/29
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericUserServiceImplTest {
    @Autowired
    GenericUserService genericUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void updatePassword() {
        String password="wezhyn";
        String username=password;
        genericUserService.modifyUserPasswordById(2, username, passwordEncoder.encode(password));

    }

}