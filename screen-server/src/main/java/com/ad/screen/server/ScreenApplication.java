package com.ad.screen.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.ad.screen.server")
@ImportResource(locations={"classpath:consumer.xml"})
public class ScreenApplication {


    public static void main(String[] args) {
        final ConfigurableApplicationContext context=new SpringApplication(ScreenApplication.class)
                .run(args);
    }
}
