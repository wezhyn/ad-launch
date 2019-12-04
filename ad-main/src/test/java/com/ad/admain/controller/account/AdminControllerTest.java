package com.ad.admain.controller.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 12.03.2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdminControllerTest {


    @Autowired
    private AdminService adminService;

    @Test
    public void info() {
        System.out.println(adminService.getByUsername("zhaoo"));
    }
}