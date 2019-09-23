package com.ad.adlaunch.config;

import com.ad.adlaunch.constants.JwtProperties;
import com.ad.adlaunch.security.*;
import com.ad.adlaunch.security.filter.AdJwtCheckAuthenticationFilter;
import com.ad.adlaunch.security.jwt.SecurityJwtProvider;
import com.ad.adlaunch.service.JwtDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Configuration
public class JwtConfig {


    @Bean
    public SecurityJwtProvider securityJwtProvider(JwtProperties jwtProperties,
                                                   JwtDetailService jwtDetailService) {
        return new SecurityJwtProvider(jwtProperties, jwtDetailService);
    }


    /**
     * 权限拦截器，当前请求无 Authentication 时，尝试从 jwt中获取
     * @param securityJwtProvider jwt 解析器
     * @param jwtProperties jwt 配置信息
     * @return filter
     */
    @Bean
    public AdJwtCheckAuthenticationFilter adJwtCheckAuthenticationFilter(
            SecurityJwtProvider securityJwtProvider,
            JwtProperties jwtProperties,
            List<IJwtRequestRead> jwtRequestReads) {

        return new AdJwtCheckAuthenticationFilter(securityJwtProvider, jwtProperties,jwtRequestReads);
    }



    @Bean(name="loginSuccessHandler")
    public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler(
            ObjectMapper objectMapper,
            SecurityJwtProvider securityJwtProvider){
        LoginAuthenticationSuccessHandler successHandler=new LoginAuthenticationSuccessHandler(securityJwtProvider, objectMapper);
        return successHandler;
    }

    @Bean(name="loginFailureHandler")
    public LoginAuthenticationFailureHandler loginAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new LoginAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public BodyJwtRequestReadImpl bodyJwtRequestRead() {
        return new BodyJwtRequestReadImpl();
    }

    @Bean
    public HeaderJwtRequestReadImpl headerJwtRequestRead() {
        return new HeaderJwtRequestReadImpl();
    }

}
