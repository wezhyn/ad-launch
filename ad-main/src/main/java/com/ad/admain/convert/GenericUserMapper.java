package com.ad.admain.convert;

import com.ad.admain.dto.UserDto;
import com.ad.admain.to.GenericUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * 实现{@link GenericUser} -> {@link UserDto} 的转变
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Mapper(
        config=CentralMapperConfig.class,
        uses={})
public interface GenericUserMapper extends AbstractMapper<GenericUser, UserDto> {

    /**
     * 实现 to -> dto 的转变
     *
     * @param genericUser mysql端对象
     * @return 前端对象
     */
    @Override
    @Mappings({
            @Mapping(source="nickName", target="nickname"),
            @Mapping(source="realName", target="realname"),
    })
    UserDto toDto(GenericUser genericUser);
}
