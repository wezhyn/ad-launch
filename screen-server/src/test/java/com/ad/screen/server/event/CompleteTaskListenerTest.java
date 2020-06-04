package com.ad.screen.server.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CompleteTaskListenerTest {
    @Autowired
    private CompleteTaskListener taskListener;

    @Test
    public void onApplicationEvent() {
        taskListener.onApplicationEvent(new CompleteTaskEvent(this, 106));
    }
}