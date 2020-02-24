package com.ad.admain.convert;

import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.AdOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @since 12.24.2019
 */
@Mapper(
        config=CentralMapperConfig.class,
        unmappedSourcePolicy=ReportingPolicy.WARN,
        unmappedTargetPolicy=ReportingPolicy.WARN
)
public interface AdOrderMapper extends AbstractMapper<AdOrder, OrderDto> {


    @Override
    @Mappings({
            @Mapping(target="username", expression="java(order.getOrderUser().getUsername())")
    })
    OrderDto toDto(AdOrder order);


}
