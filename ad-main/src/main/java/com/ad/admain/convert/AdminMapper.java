package com.ad.admain.convert;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.controller.account.dto.AdminDto;
import com.ad.admain.controller.account.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Mapper(config=CentralMapperConfig.class,
        unmappedTargetPolicy=ReportingPolicy.IGNORE)
public abstract class AdminMapper implements AbstractMapper<Admin, AdminDto> {

    @Autowired
    protected QiNiuProperties qiNiuProperties;

    /**
     * 实现 to -> dto 的转变
     * 子类覆盖该方法，添加{@link Mappings} 来添加不同名的匹配
     *
     * @param admin mysql端对象
     * @return 前端对象
     */
    @Override
    @Mappings({
            @Mapping(target="avatar",
                    expression="java(qiNiuProperties.getHostName() + \"/\" + admin.getAvatar() )"),
//            @Mapping(source="enable", target="status"),
    })
    public abstract AdminDto toDto(Admin admin);
}
