package com.ad.admain.controller.income;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DeliverIncomeServiceTest {

    @Autowired
    private DeliverIncomeService deliverIncomeService;

    @Test
    public void weekRevenue() {
        final List<DriverInComeDayRecord> sdf = deliverIncomeService.weekRevenue(1, LocalDate.now());
        System.out.println(sdf);
    }
}