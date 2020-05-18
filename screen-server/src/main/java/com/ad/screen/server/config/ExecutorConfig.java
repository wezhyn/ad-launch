package com.ad.screen.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
@Configuration
public class ExecutorConfig {


    /**
     * 执行任务
     *
     * @return executor
     */
    @Bean(name = "self_taskExecutor")
    public ScheduledExecutorService threadPoolTaskExecutor() {
        return new ScheduledThreadPoolExecutor(2, t -> new Thread(t, "self_async_task"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
