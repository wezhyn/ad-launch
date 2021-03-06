package com.ad.admain.convert;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.controller.account.CertificationCardVo;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.controller.account.user.UserDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * 实现{@link GenericUser} -> {@link UserDto} 的转变
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Mapper(
        config=CentralMapperConfig.class,
        uses={},
        unmappedSourcePolicy=ReportingPolicy.IGNORE)
public abstract class GenericUserMapper implements AbstractMapper<GenericUser, UserDto> {

    @Autowired
    protected QiNiuProperties qiNiuProperties;

    /**
     * 实现 to -> dto 的转变
     *
     * @param genericUser mysql端对象
     * @return 前端对象
     */
    @Override
    @Mappings({
            @Mapping(source = "nickName", target = "nickname"),
            @Mapping(source = "realName", target = "realname"),
            @Mapping(source = "birthDay", target = "birthday"),
            @Mapping(source = "enable", target = "status"),
            @Mapping(source = "sex", target = "gender"),
            @Mapping(target = "avatar",
                    expression = "java(qiNiuProperties.getHostName() + \"/\" + genericUser.getAvatar() )")
    })
    public abstract UserDto toDto(GenericUser genericUser);


    @Mappings({
            @Mapping(source = "nickName", target = "nickname"),
            @Mapping(source = "realName", target = "realname"),
            @Mapping(source = "birthDay", target = "birthday"),
            @Mapping(source = "enable", target = "status"),
            @Mapping(source = "sex", target = "gender"),
            @Mapping(target = "avatar",
                    expression = "java(qiNiuProperties.getHostName() + \"/\" + genericUser.getAvatar() )"),
            @Mapping(target = "idCardPreImg",
                    expression = "java(qiNiuProperties.getHostName() + \"/\" + genericUser.getIdCardPreImg() )"),
            @Mapping(target = "idCardAftImg",
                    expression = "java(qiNiuProperties.getHostName() + \"/\" + genericUser.getIdCardAftImg() )")
    })
    public abstract CertificationCardVo toDtoWithCard(GenericUser genericUser);


    @Override
    @Mapping(target = "equipmentList", ignore = true)
    @InheritInverseConfiguration(name = "toDto")
    @Mapping(target = "certificationCard", ignore = true)
    public abstract GenericUser toTo(UserDto userDto);
}
