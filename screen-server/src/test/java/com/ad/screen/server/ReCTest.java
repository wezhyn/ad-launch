package com.ad.screen.server;

import com.ad.launch.order.RemoteRevenueServiceI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReCTest {

    @Resource
    private RemoteRevenueServiceI remoteRevenueServiceI;

    @Test
    public void test() {
        System.out.println(remoteRevenueServiceI.getRevenueConfig());
    }
}
