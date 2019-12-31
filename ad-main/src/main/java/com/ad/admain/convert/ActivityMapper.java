package com.ad.admain.convert;

import com.ad.admain.controller.activity.Activity;
import com.ad.admain.controller.activity.ActivityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@Mapper(config=CentralMapperConfig.class,
        uses={AdminMapper.class},
        unmappedTargetPolicy=ReportingPolicy.IGNORE,
        unmappedSourcePolicy=ReportingPolicy.IGNORE)
public abstract class ActivityMapper implements AbstractMapper<Activity, ActivityDto> {


    @Override
    @Mapping(source="admin", target="admin")
    @Mapping(source="aid", target="aid")
    public abstract ActivityDto toDto(Activity activity);
}
