package com.wezhyn.project.database.impl;

import com.wezhyn.project.database.EnumMapperFactory;
import com.wezhyn.project.database.PrimitiveEnumMapper;
import com.wezhyn.project.database.TypeEnumMapper;

/**
 * 用于 {@link Enum#name()}
 *
 * @author wezhyn
 * @since 12.12.2019
 */
public class EnumStringMapperFactory implements EnumMapperFactory<Enum<?>, String> {
    @Override
    @SuppressWarnings("unchecked")
    public PrimitiveEnumMapper<Enum<?>, String> getPrimitiveEnumMapper(Class<Enum<?>> enumClass) {
        return name->{
            if (name==null) {
                return null;
            }
            final Class eClass=enumClass;
            return Enum.valueOf(eClass, name.trim());
        };
    }

    @Override
    public TypeEnumMapper<Enum<?>, String> getTypeEnumMapper() {
        return Enum::name;
    }
}
