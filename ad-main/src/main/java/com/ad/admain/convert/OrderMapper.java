package com.ad.admain.convert;

import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.Order;
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
public abstract class OrderMapper implements AbstractMapper<Order, OrderDto> {


    @Override
    @Mappings({
            @Mapping(target="username", expression="java(order.getOrderUser().getUsername())")
    })
    public abstract OrderDto toDto(Order order);


}
