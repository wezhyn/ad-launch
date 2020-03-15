package com.ad.admain.config;

import com.ad.admain.controller.FileUploadService;
import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.impl.QiNiuFileUploadServiceImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Configuration
public class BeanConfig {


    @Autowired
    private ObjectMapper objectMapper;




    @Bean
    @ConditionalOnMissingBean(value={FileUploadService.class})
    @ConditionalOnProperty(prefix="custom.qn", name={"access-key", "secret-key"})
    public FileUploadService fileUploadService(QiNiuProperties qiNiuProperties, GenericUserService genericUserService) {
        return new QiNiuFileUploadServiceImpl(qiNiuProperties, genericUserService);
    }







}
