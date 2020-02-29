package com.ad.admain.controller.pay.convert;

import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @since 12.24.2019
 */
@Mapper(
        config=CentralMapperConfig.class,
        imports={ProduceMapper.class},
        unmappedSourcePolicy=ReportingPolicy.WARN,
        unmappedTargetPolicy=ReportingPolicy.WARN
)
public interface AdOrderMapper extends AbstractMapper<AdOrder, OrderDto> {


    @Override
    @Mapping(target="produceId", expression="java(order.getProduce().getId())")
    OrderDto toDto(AdOrder order);


}
