package com.ad.admain.controller.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ActivityServiceImplTest {

    @Autowired
    private ActivityService activityService;

    @Test
    public void save() {
        final Activity ac=Activity.builder()
                .aid(2)
                .publish(true)
                .content("<html> <body>xxx </body></html>")
                .title("title").build();
        activityService.save(ac);
    }

    @Test
    public void get() {
        final Optional<Activity> up=activityService.getById(1);
        System.out.println(up.isPresent());
    }
}