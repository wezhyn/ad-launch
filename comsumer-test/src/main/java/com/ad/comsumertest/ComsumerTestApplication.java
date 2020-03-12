package com.ad.comsumertest;

import com.ad.launch.user.AdUser;
import com.ad.launch.user.exception.NotUserException;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations={"classpath:consumer.xml"})
@EnableDubbo
public class ComsumerTestApplication {


    public static void main(String[] args) {
        final ConfigurableApplicationContext run=SpringApplication.run(ComsumerTestApplication.class, args);
        final ComsumerTest test=run.getBean(ComsumerTest.class);
        try {
            final AdUser user=test.load(1);
            System.out.println(user);
        } catch (NotUserException e) {
            e.printStackTrace();
        }

    }


}
