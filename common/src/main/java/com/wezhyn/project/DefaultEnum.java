package com.wezhyn.project;

/**
 * 用于持久化枚举类的默认值
 *
 * @author wezhyn
 * @since 01.01.2020
 */
public interface DefaultEnum<T> {


    T defaultValue();
}
