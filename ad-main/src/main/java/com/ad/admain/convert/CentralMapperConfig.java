package com.ad.admain.convert;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@MapperConfig(
        componentModel="spring",
        uses={StringEnumMapper.class},
        unmappedSourcePolicy=ReportingPolicy.WARN,
        unmappedTargetPolicy=ReportingPolicy.ERROR,
//        ,typeConversionPolicy=ReportingPolicy.WARN
        mappingInheritanceStrategy=MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG
)
public interface CentralMapperConfig {


}
