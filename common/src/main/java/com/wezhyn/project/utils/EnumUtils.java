package com.wezhyn.project.utils;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对自定义枚举类 {@link StringEnum} 和 {@link NumberEnum}
 * 的反射，通过{#getValue} 和{#getOridinal} 获取对应的枚举
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
public final class EnumUtils {


    /**
     * 返回 BaseEnum 子类中 value对应的枚举类
     *
     * @param tClass StringEnum 的子类
     * @param name   value
     * @param <T>    T extends BaseEnum
     * @return BaseEnum
     */
    public static <T extends Enum<T> & StringEnum> T valueOfStringEnumIgnoreCase(Class<T> tClass, String name) {
        T[] enums=tClass.getEnumConstants();
        return Arrays.stream(enums)
                .filter(t->t.getValue().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(()->new EnumConstantNotPresentException(tClass, name));
    }


    public static <T extends Enum<T> & NumberEnum> T valueOfNumberEnum(Class<T> tClass, Number o) {
        T[] enums=tClass.getEnumConstants();
        return Arrays.stream(enums)
                .filter(t->Objects.equals(o, t.getNumber()))
                .findFirst()
                .orElseThrow(()->new EnumConstantNotPresentException(tClass, String.valueOf(o)));
    }
}
