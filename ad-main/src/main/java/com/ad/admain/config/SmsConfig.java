package com.ad.admain.config;

import com.ad.admain.controller.impl.LogSmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wezhyn
 * @since 02.23.2020
 */
@Configuration
public class SmsConfig {


    @Bean
    public LogSmsService smsService() {
        return new LogSmsService();
    }
}
