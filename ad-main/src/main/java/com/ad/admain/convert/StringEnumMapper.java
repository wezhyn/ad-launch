package com.ad.admain.convert;

import com.ad.admain.enumate.StringEnum;
import com.ad.admain.utils.EnumUtils;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Mapper(componentModel="spring")
class StringEnumMapper {

    /**
     * 用于在 {@link com.ad.admain.enumate.StringEnum} 与 {@link String}
     * 之间进行转换
     *
     * @param value  {@link StringEnum#getValue()}
     * @param tClass 实现了 {@link StringEnum} 的子类
     * @param <T>    tClass
     * @return Enum<?>
     */
    <T extends Enum<?> & StringEnum> T stringEnum(String value, @TargetType Class<T> tClass) {
        if (value==null) {
            return null;
        }
        return EnumUtils.valueOfBaseEnumIgnoreCase(tClass, value);
    }

    <T extends Enum<?> & StringEnum> String getStringEnumValue(T stringEnum) {
        if (stringEnum==null) {
            return "";
        }
        return stringEnum.getValue();
    }
}
