package com.ad.screen.server.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 05.10.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LocalResumeServerListenerTest {

    @Autowired
    private LocalResumeServerListener localResumeServerListener;

    @Test
    public void updateResumeCount() throws InterruptedException {

        TimeUnit.SECONDS.sleep(5);
        localResumeServerListener.updateResumeCount(0);
        TimeUnit.HOURS.sleep(1);

    }
}