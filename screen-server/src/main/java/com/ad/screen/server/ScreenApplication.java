package com.ad.screen.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@SpringBootApplication
@EnableJpaRepositories
@ImportResource(locations={"classpath:consumer.xml"})
@EnableDubbo
@EnableScheduling
public class ScreenApplication {


    public static void main(String[] args) {
        try {
            SpringApplication application=new SpringApplication(ScreenApplication.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            final ConfigurableApplicationContext context=application.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
