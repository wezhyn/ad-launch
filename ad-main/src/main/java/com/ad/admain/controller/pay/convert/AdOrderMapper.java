package com.ad.admain.controller.pay.convert;

import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.AdProduce;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.CentralMapperConfig;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

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
public interface AdOrderMapper extends AbstractMapper<AdOrder, OrderDto> {


    @Override
    @Mapping(target = "produceId", expression = "java(order.getProduce().getId())")
    @Mapping(target = "produceContext", source = "produce.produceContext")
    OrderDto toDto(AdOrder order);

    @Override
    @Mapping(target = "produceContext", ignore = true)
    @Mapping(target = "produce", expression = "java(this.produce(orderDto))")
    @InheritInverseConfiguration(name = "toDto")
    AdOrder toTo(OrderDto orderDto);

    default AdProduce produce(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        AdProduce.AdProduceBuilder adProduce = AdProduce.builder();

        adProduce.id(orderDto.getProduceId());
        List<String> list = orderDto.getProduceContext();
        if (list != null) {
            adProduce.produceContext(new ArrayList<String>(list));
        }
        return adProduce.build();
    }
}
