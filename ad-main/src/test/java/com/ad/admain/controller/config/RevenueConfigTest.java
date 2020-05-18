package com.ad.admain.controller.config;

import com.ad.launch.order.RevenueConfig;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
public class RevenueConfigTest {
    @Test
    public void index() {
        final int time = new RevenueConfig().revenueScope(LocalDateTime.of(LocalDate.now(),
                LocalTime.of(20, 0)));
        System.out.println(time);
        System.out.println(new RevenueConfig().revenue(time));
    }

    @Test
    public void toJson() throws IOException {
        System.out.println(new RevenueConfig().toJson());
        System.out.println(RevenueConfig.fromJson(new RevenueConfig().toJson()));
    }
}