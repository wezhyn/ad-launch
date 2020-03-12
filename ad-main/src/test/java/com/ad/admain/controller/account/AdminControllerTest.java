package com.ad.admain.controller.account;

import com.ad.admain.controller.account.administrator.Admin;
import com.ad.admain.controller.account.administrator.AdminService;
import com.wezhyn.project.account.SexEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 12.03.2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdminControllerTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    @Test
    public void test() {
        Admin zhaoo=Admin.newBuilder()
                .username("zhaoo")
                .password(passwordEncoder.encode("zhaoo"))
                .sex(SexEnum.MALE)
                .build();
        adminService.save(zhaoo);
    }

    @Test
    public void info() {
        System.out.println(adminService.getByUsername("zhaoo"));
    }
}