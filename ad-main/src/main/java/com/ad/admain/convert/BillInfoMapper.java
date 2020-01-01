package com.ad.admain.convert;

import com.ad.admain.controller.pay.dto.BillInfoDto;
import com.ad.admain.controller.pay.to.BillInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @since 01.01.2020
 */
@Mapper(config=CentralMapperConfig.class,
        unmappedTargetPolicy=ReportingPolicy.IGNORE)
public abstract class BillInfoMapper implements AbstractMapper<BillInfo, BillInfoDto> {
}
