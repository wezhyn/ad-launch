package com.wezhyn.project.converter;

/**
 * @author wezhyn
 * @since 12.12.2019
 */
public interface TypeConverter<S, T> {
    boolean canConvert(Class<?> targetType);

    T convert(S source, Class<T> targetType);

}

