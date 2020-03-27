package com.ad.screen.server.service;

import com.ad.screen.server.entity.TaskKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EquipTaskServiceTest {

    @Autowired
    private EquipTaskService equipTaskService;

    @Test
    public void mergeTaskExecStatistics() throws InterruptedException {

        final ExecutorService exe=Executors.newCachedThreadPool();
        for (int i=0; i < 100; i++) {
            exe.submit(()->{
                equipTaskService.mergeTaskExecStatistics(new TaskKey(73, 1), 1);
            });
        }
        TimeUnit.MINUTES.sleep(5);
    }
}