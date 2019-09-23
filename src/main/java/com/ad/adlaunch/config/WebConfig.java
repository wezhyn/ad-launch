package com.ad.adlaunch.config;

import com.ad.adlaunch.constants.ResourceConstant;
import com.ad.adlaunch.convert.GenericStringEnumConvert;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceMatch=ResourceConstant.RESOURCE + "/**";

        registry.addResourceHandler(resourceMatch).addResourceLocations(resourceDirect);
    }
*/


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new GenericStringEnumConvert());
    }


}
