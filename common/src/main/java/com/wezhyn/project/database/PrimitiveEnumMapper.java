package com.wezhyn.project.database;

/**
 * @author wezhyn
 * @since 12.11.2019
 */
public interface PrimitiveEnumMapper<T, R> {


    /**
     * T 枚举类型
     *
     * @param primitive 基本类型：Number、String
     * @return 持久化枚举类型
     */
    T apply(R primitive);
}
