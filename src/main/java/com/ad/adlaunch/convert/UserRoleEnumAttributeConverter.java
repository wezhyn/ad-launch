package com.ad.adlaunch.convert;

import com.ad.adlaunch.enumate.AuthenticationEnum;
import com.ad.adlaunch.utils.EnumUtils;
import org.springframework.security.core.Authentication;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 单独配置一个转化类
 *
 * @author wezhyn
 */
@Converter(autoApply=true)
public class UserRoleEnumAttributeConverter implements AttributeConverter<AuthenticationEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AuthenticationEnum userRoleEnum) {
        if (userRoleEnum==null) {
            return AuthenticationEnum.USER.getOrdinal();
        }
        return userRoleEnum.getOrdinal();
    }

    @Override
    public AuthenticationEnum convertToEntityAttribute(Integer i) {
        return EnumUtils.valueOfBaseEnum(AuthenticationEnum.class, i);
    }
}