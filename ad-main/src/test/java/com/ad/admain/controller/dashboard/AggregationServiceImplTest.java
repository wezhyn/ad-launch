package com.ad.admain.controller.dashboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
    public void getDayAggregation() {
        final Optional<AggregationDto> dayAggregation=aggregationService.getDayAggregation(LocalDate.now().minusDays(1L));
        System.out.println(dayAggregation);
    }

    @Test
    public void getHourAggregation() {

        final AggregationDto hourAggregation=aggregationService.getHourAggregation();
        System.out.println(hourAggregation);
    }


    @Test
    public void event() throws InterruptedException {
        AggregationServiceImpl service=(AggregationServiceImpl) aggregationService;
        service.propagateAggregation(DateType.HOUR, LocalDateTime.now());
        Thread.sleep(10000);
    }
}