package com.wezhyn.project.database.impl;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.database.EnumMapperFactory;
import com.wezhyn.project.database.PrimitiveEnumMapper;
import com.wezhyn.project.database.TypeEnumMapper;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author wezhyn
 * @since 12.12.2019
 */
public class NumberEnumMapperFactory implements EnumMapperFactory<NumberEnum, Integer> {
    @Override
    public PrimitiveEnumMapper<NumberEnum, Integer> getPrimitiveEnumMapper(Class<Enum<?>> enumClass) {
        if (!NumberEnum.class.isAssignableFrom(enumClass)) {
            throw new RuntimeException("错误的枚举类型");
        }
        return number->{
            if (number==null) {
                return null;
            }
            Enum<?>[] enums=enumClass.getEnumConstants();
            return Arrays.stream(enums)
                    .filter(t->{
                        NumberEnum numberEnum=(NumberEnum) t;
                        return Objects.equals(numberEnum.getNumber(), number);
                    })
                    .findFirst()
                    .map(e->(NumberEnum) e)
                    .orElseThrow(()->new EnumConstantNotPresentException(enumClass, number.toString()));

        };

    }

    @Override
    public TypeEnumMapper<NumberEnum, Integer> getTypeEnumMapper() {
        return NumberEnum::getNumber;
    }
}
