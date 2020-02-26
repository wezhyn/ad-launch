package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.dto.RefundOrderDto;
import com.ad.admain.controller.pay.to.RefundOrder;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface RefundMapper extends AbstractMapper<RefundOrderDto, RefundOrder> {
}
