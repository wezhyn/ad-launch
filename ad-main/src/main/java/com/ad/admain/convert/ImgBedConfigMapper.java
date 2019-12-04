package com.ad.admain.convert;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.dto.ConfigDto;
import com.ad.admain.to.ImgBed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Mapper(config=CentralMapperConfig.class,
        unmappedTargetPolicy=ReportingPolicy.IGNORE)
public abstract class ImgBedConfigMapper implements AbstractMapper<ImgBed, ConfigDto> {

    @Autowired
    QiNiuProperties qiNiuProperties;

    @Override
    @Mappings({
            @Mapping(source="index", target="key"),
            @Mapping(target="value",
                    expression="java(qiNiuProperties.getHostName() + \"/\" + imgBed.getAddress() )")
    })
    public abstract ConfigDto toDto(ImgBed imgBed);


    @Override
    @Mappings({
            @Mapping(source="value", target="address")
    })
    public abstract ImgBed toTo(ConfigDto configDto);
}
