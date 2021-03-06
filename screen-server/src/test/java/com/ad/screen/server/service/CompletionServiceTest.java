package com.ad.screen.server.service;

import com.ad.screen.server.entity.TaskKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CompletionServiceTest {

    @Autowired
    private CompletionService completionService;

    @Test
    public void completeNumIncr() {
        completionService.tryComplete(new TaskKey(98, 1));

    }
}