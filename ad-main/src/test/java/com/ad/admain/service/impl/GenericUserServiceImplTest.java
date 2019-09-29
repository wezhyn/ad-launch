package com.ad.admain.service.impl;

import com.ad.admain.service.GenericUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
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
    @Test
    public void modifyUserPassword() {
        int  i = genericUserService.modifyUserPassword("wezhyn","fuck");
        System.out.println(i);
    }
}