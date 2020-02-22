package com.ad.admain;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.config.QqCloudSmsProperties;
import com.ad.admain.config.web.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
@SpringBootApplication(scanBasePackages="com.ad", exclude={SecurityAutoConfiguration.class})
@EnableTransactionManagement()
@EnableConfigurationProperties(value={JwtProperties.class, QiNiuProperties.class, QqCloudSmsProperties.class})
public class AdLaunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdLaunchApplication.class, args);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    //添加quartz启动servlet
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean(){
//        ServletRegistrationBean servletRegistrationBean =  new ServletRegistrationBean(
//                new QuartzInitializerServlet());
//        servletRegistrationBean.addInitParameter("shutdown-on-unload","true");
//        servletRegistrationBean.setLoadOnStartup(2);
//        return  servletRegistrationBean;
//    }

}
