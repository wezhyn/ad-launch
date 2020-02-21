package com.ad.admain.controller.dashboard.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.21.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccurateStrategyTest {


    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void isAccurate() {
        AccurateStrategy.MysqlCalculateAccurate accurate=new AccurateStrategy.MysqlCalculateAccurate(entityManager);
        accurate.isAccurate(LocalDateTime.of(2020, 2, 10, 3, 0));
    }
}