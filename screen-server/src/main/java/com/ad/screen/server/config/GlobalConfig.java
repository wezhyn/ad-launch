package com.ad.screen.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Configuration
public class GlobalConfig {

    @Bean
    public GlobalIdentify globalIdentify() {
        return GlobalIdentify.IDENTIFY;
    }
}
