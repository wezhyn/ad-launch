package com.ad.admain.controller.pay.convert;

import com.ad.admain.controller.pay.dto.RefundOrderDto;
import com.ad.admain.controller.pay.to.RefundOrder;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author : wezhyn
 * @date : 2020/2/27
 */
@Mapper(
        config=CentralMapperConfig.class,
        unmappedSourcePolicy=ReportingPolicy.WARN,
        unmappedTargetPolicy=ReportingPolicy.WARN
)
public interface RefundMapper extends AbstractMapper<RefundOrderDto, RefundOrder> {


    @Override
    @Mapping(target="refundAmount", source="totalAmount")
    RefundOrderDto toTo(RefundOrder refundOrder);
}
