package com.wezhyn.project.database;

/**
 * EnumType 持久到具体类的策略接口
 * Enum -> Primitive
 *
 * @author wezhyn
 * @since 12.11.2019
 */
public interface TypeEnumMapper<T, R> {

    /**
     * T 枚举类型
     *
     * @param concreteEnum 枚举
     * @return 持久化枚举类型
     */
    R apply(T concreteEnum);
}
