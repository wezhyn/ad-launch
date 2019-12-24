package com.wezhyn.project.annotation;

import com.wezhyn.project.database.EnumType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举类型的转变策略，具体实现类应该继承 {@link com.wezhyn.project.StringEnum} 或者 {@link com.wezhyn.project.NumberEnum}
 * 如果未继承 上述两个类，{@code NUMBER} 采用{@link Enum#ordinal()},{@code String} 对应 {@link Enum#toString()}
 *
 * @author wezhyn
 * @since 12.11.2019
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyEnum {

    /**
     * 默认以 String 类型持久化 枚举
     *
     * @return EnumType
     */
    EnumType value() default EnumType.STRING;
}
