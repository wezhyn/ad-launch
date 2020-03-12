package com.ad.admain.config;

import com.ad.admain.controller.FileUploadService;
import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.impl.QiNiuFileUploadServiceImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;

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

    @PostConstruct
    public void init() {
        this.initObjectMappper();
    }

    @Async
    public void initObjectMappper() {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule module=new SimpleModule("simpleModule");
        this.objectMapper.registerModule(module);
    }


}
