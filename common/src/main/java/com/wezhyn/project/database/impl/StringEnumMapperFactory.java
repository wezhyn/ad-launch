package com.wezhyn.project.database.impl;

import com.wezhyn.project.StringEnum;
import com.wezhyn.project.database.EnumMapperFactory;
import com.wezhyn.project.database.PrimitiveEnumMapper;
import com.wezhyn.project.database.TypeEnumMapper;

import java.util.Arrays;

/**
 * 用于 {@link StringEnum#getValue()}
 *
 * @author wezhyn
 * @since 12.12.2019
 */
public class StringEnumMapperFactory implements EnumMapperFactory<StringEnum, String> {

    private static final TypeEnumMapper<StringEnum, String> DEFAULT_ENUM_TO_STRING=StringEnum::getValue;

    @Override
    public PrimitiveEnumMapper<StringEnum, String> getPrimitiveEnumMapper(final Class<Enum<?>> enumClass) {
        if (!StringEnum.class.isAssignableFrom(enumClass)) {
            throw new RuntimeException("错误的枚举类型");
        }
        return name->{
            if (name==null) {
                return null;
            }
            Enum<?>[] enums=enumClass.getEnumConstants();
            return Arrays.stream(enums)
                    .filter(t->{
                        StringEnum stringEnum=(StringEnum) t;
                        return stringEnum.getValue().equalsIgnoreCase(name);
                    })
                    .findFirst()
                    .map(e->(StringEnum) e)
                    .orElseThrow(()->new EnumConstantNotPresentException(enumClass, name));

        };
    }

    @Override
    public TypeEnumMapper<StringEnum, String> getTypeEnumMapper() {
        return DEFAULT_ENUM_TO_STRING;
    }
}
