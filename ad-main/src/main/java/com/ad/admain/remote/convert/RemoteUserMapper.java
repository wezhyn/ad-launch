package com.ad.admain.remote.convert;

import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.CentralMapperConfig;
import com.ad.launch.user.AdUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Mapper(
        config=CentralMapperConfig.class,
        unmappedSourcePolicy=ReportingPolicy.WARN,
        unmappedTargetPolicy=ReportingPolicy.WARN
)
public interface RemoteUserMapper extends AbstractMapper<GenericUser, AdUser> {


}
