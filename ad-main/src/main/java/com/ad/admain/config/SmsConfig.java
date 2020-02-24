package com.ad.admain.config;

import com.ad.admain.controller.ISmsService;
import com.ad.admain.controller.impl.LogSmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wezhyn
 * @since 02.23.2020
 */
@Configuration
public class SmsConfig {


/*
    @Autowired
    private QqCloudSmsProperties qqCloudSmsProperties;

    @Bean
    public ISmsService smsService() {
        return new QCloudSmsService(qqCloudSmsProperties);
    }
*/

    @Bean
    public ISmsService smsService() {
        return new LogSmsService();
    }
}
