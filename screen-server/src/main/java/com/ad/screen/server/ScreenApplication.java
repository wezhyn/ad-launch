package com.ad.screen.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@SpringBootApplication
@EnableJpaRepositories
@ImportResource(locations={"classpath:consumer.xml"})
@EnableDubbo
public class ScreenApplication {


    public static void main(String[] args) {

            SpringApplication application=new SpringApplication(ScreenApplication.class);
            application.run(args);


    }
}
