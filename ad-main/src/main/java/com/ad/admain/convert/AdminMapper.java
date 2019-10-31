package com.ad.admain.convert;

import com.ad.admain.dto.AdminDto;
import com.ad.admain.to.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Mapper(config=CentralMapperConfig.class,
        unmappedTargetPolicy=ReportingPolicy.IGNORE)
public interface AdminMapper extends AbstractMapper<Admin, AdminDto> {

    /**
     * 实现 to -> dto 的转变
     * 子类覆盖该方法，添加{@link Mappings} 来添加不同名的匹配
     *
     * @param admin mysql端对象
     * @return 前端对象
     */
    @Override
    @Mappings({
    })
    AdminDto toDto(Admin admin);
}
