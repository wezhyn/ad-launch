package com.ad.admain.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wezhyn
 * @date 2019/10/06
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class MapStructMapperExtractor {

    private Map<Class<?>, AbstractMapper<?, ?>> mappers;

    private MapStructMapperExtractor(List<? extends AbstractMapper<?, ?>> mappers) {
        this.mappers=new HashMap<>(16);
        for (AbstractMapper abstractMapper : mappers) {
//          // 反射 AbstractMapper<SOURCE,TARGET> 中的泛型信息
            ResolvableType mapperType=ResolvableType.forClass(abstractMapper.getClass())
                    .as(AbstractMapper.class);
            if (mapperType!=null) {
//                获取 SOURCE class
                Class<?> sourceClass=mapperType.getGeneric(0).getRawClass();
                this.mappers.put(sourceClass, abstractMapper);
            }
        }
    }


}
