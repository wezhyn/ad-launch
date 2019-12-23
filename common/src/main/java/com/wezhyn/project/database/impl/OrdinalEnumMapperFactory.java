package com.wezhyn.project.database.impl;

import com.wezhyn.project.database.EnumMapperFactory;
import com.wezhyn.project.database.PrimitiveEnumMapper;
import com.wezhyn.project.database.TypeEnumMapper;

/**
 * @author wezhyn
 * @since 12.12.2019
 */
public class OrdinalEnumMapperFactory implements EnumMapperFactory<Enum, Integer> {
    @Override
    public PrimitiveEnumMapper<Enum, Integer> getPrimitiveEnumMapper(Class<Enum<?>> enumClass) {
        return ordinal->{
            Enum<?>[] enums=enumClass.getEnumConstants();
            if (enums==null) {
                throw new RuntimeException("加载枚举类型失败");
            }
            if (ordinal < 0 || ordinal >= enums.length) {
                throw new IllegalArgumentException(
                        String.format(
                                "Unknown ordinal value [%s] for enum class [%s]",
                                ordinal,
                                enumClass.getName()
                        )
                );
            }
            return enums[ordinal];
        };
    }

    @Override
    public TypeEnumMapper<Enum, Integer> getTypeEnumMapper() {
        return Enum::ordinal;
    }
}
