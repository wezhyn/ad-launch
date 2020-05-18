package com.ad.admain;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.config.QqCloudSmsProperties;
import com.ad.admain.config.web.JwtProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
@SpringBootApplication(scanBasePackages="com.ad.*", exclude={SecurityAutoConfiguration.class})
@EnableCaching
@EnableTransactionManagement()
@EnableDubbo
@ImportResource(locations = {"classpath:provoder.xml"})
@EnableConfigurationProperties(value={JwtProperties.class, QiNiuProperties.class, QqCloudSmsProperties.class})
public class AdLaunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdLaunchApplication.class, args);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
