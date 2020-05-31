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
        final LocalDate noe = LocalDate.now();

        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(6, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(7, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(15, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(16, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(17, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(18, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(19, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(20, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(21, 59))));
        System.out.println(RevenueConfig.revenueScope(LocalDateTime.of(noe, LocalTime.of(22, 59))));
    }

    @Test
    public void toJson() throws IOException {
        System.out.println(new RevenueConfig().toJson());
        System.out.println(RevenueConfig.fromJson(new RevenueConfig().toJson()));
    }
}