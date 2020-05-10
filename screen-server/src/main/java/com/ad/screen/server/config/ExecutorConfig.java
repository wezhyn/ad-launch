package com.ad.screen.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("self_async_task");
        executor.initialize();
        return executor;
    }

    /**
     * 处理本地重启任务
     *
     * @return executor
     */
    @Bean(name = "resume_server")
    public ExecutorService resumeTaskExecutors() {
        return Executors.newSingleThreadExecutor(r -> {
            final Thread thread = new Thread(r, "resume-thread");
            thread.setDaemon(true);
            return thread;
        });
    }
}
