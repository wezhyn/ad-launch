package com.ad.adlaunch.config;

import com.ad.adlaunch.constants.QiNiuProperties;
import com.ad.adlaunch.enumate.BaseEnum;
import com.ad.adlaunch.repository.GenericUserRepository;
import com.ad.adlaunch.service.FileUploadService;
import com.ad.adlaunch.service.impl.QiNiuFileUploadServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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
