package com.ad.admain.config;

import com.ad.admain.constants.QiNiuProperties;
import com.ad.admain.repository.GenericUserRepository;
import com.ad.admain.service.FileUploadService;
import com.ad.admain.service.impl.QiNiuFileUploadServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private ObjectMapper objectMapper;


    @Bean
    @ConditionalOnMissingBean(value={FileUploadService.class})
    @ConditionalOnProperty(prefix="custom.qn" ,name={"access-key","secret-key"})
    public FileUploadService fileUploadService(QiNiuProperties qiNiuProperties, GenericUserRepository userRepository) {
        return new QiNiuFileUploadServiceImpl(qiNiuProperties,userRepository);
    }

    @PostConstruct
    public void init() {
        this.initObjectMappper();
    }

    @Async
    public void initObjectMappper() {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        SimpleModule module=new SimpleModule("simpleModule");
        this.objectMapper.registerModule(module);
    }



}
