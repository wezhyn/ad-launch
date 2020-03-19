package com.ad.screen.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * @ClassName InitialServiceTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/19 23:38
 * @Version V1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitialServiceTest {
@Autowired
InitialService initialService;
    @Test
    public void afterDownTime() {
        try {
            initialService.afterDownTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}