package com.ad.admain.remote.convert;

import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.CentralMapperConfig;
import com.ad.launch.order.AdRemoteOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @ClassName RemoteOrderMapper
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/17 19:25
 * @Version V1.0
 **/

@Mapper(
        config= CentralMapperConfig.class,
        unmappedSourcePolicy= ReportingPolicy.WARN,
        unmappedTargetPolicy=ReportingPolicy.WARN
)
public interface RemoteOrderMapper extends AbstractMapper<AdOrder, AdRemoteOrder> {
    @Override
    AdRemoteOrder toDto(AdOrder adOrder);
}
