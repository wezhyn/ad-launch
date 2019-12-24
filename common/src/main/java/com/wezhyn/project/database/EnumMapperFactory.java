package com.wezhyn.project.database;

/**
 * @author wezhyn
 * @since 12.11.2019
 */
public interface EnumMapperFactory<T, R> {

    /**
     * 获取 primitive -> Enum
     *
     * @param enumClass 具体枚举类型
     * @return {@link PrimitiveEnumMapper}
     */
    PrimitiveEnumMapper<T, R> getPrimitiveEnumMapper(Class<Enum<?>> enumClass);

    /**
     * 获取 enum -> primitive
     *
     * @return {@link TypeEnumMapper}
     */
    TypeEnumMapper<T, R> getTypeEnumMapper();
}
