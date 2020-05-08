package com.ad.admain.controller.pay.convert;

import com.ad.admain.controller.pay.dto.OrderDtoWithUser;
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
        config = CentralMapperConfig.class,
        imports = {ProduceMapper.class},
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface AdOrderWithUserMapper extends AbstractMapper<AdOrder, OrderDtoWithUser> {


    @Override
    @Mapping(target = "produceId", expression = "java(order.getProduce().getId())")
    @Mapping(target = "username", expression = "java(order.getOrderUser().getUsername())")
    OrderDtoWithUser toDto(AdOrder order);


}
