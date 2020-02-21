package com.ad.admain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author wezhyn
 * @since 02.20.2020
 */
@Configuration
@EnableAsync
public class SyncConfig {


    /**
     * 执行任务
     *
     * @return executor
     */
    @Bean(name="taskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(20);
        executor.setThreadNamePrefix("spring_async_task");
        executor.initialize();
        return executor;
    }

    /**
     * 用来发布任务
     *
     * @return executor
     */
    @Bean(name="publish_task_executor")
    public TaskExecutor threadPoolTaskExecutorOther() {
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(20);
        executor.setThreadNamePrefix("spring_async_publish");
        executor.initialize();
        return executor;
    }
}
