package com.ad.admain.controller.dashboard;

import com.ad.admain.controller.dashboard.service.AggregationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author wezhyn
 * @since 01.23.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AggregationServiceImplTest {

    @Autowired
    private AggregationService aggregationService;


    @Test
    public void getHour() throws InterruptedException, ExecutionException {
        final LocalDateTime searchTime=LocalDateTime.of(2020, 2, 20, 6, 0);
        final Future<AggregationDto> hourAggregation=aggregationService.getMonthAggregation(searchTime);
        System.out.println(hourAggregation.get());
    }
}