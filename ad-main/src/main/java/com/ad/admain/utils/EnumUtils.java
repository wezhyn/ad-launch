package com.ad.admain.utils;

import com.ad.admain.enumate.BaseEnum;
import com.ad.admain.enumate.StringEnum;

import java.util.Arrays;

/**
 * 对自定义枚举类 {@link com.ad.admain.enumate.StringEnum} 和 {@link BaseEnum}
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
     * @param name value
     * @param <T> T extends BaseEnum
     * @return BaseEnum
     */
    public static <T extends Enum & StringEnum> T valueOfBaseEnumIgnoreCase(Class<T> tClass, String name) {
        T[] enums=tClass.getEnumConstants();
        return Arrays.stream(enums)
                .filter(t->t.getValue().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(()->new EnumConstantNotPresentException(tClass, name));
    }

    public static <T extends Enum & BaseEnum> T valueOfBaseEnum(Class<T> tClass, Integer o) {
        T[] enums=tClass.getEnumConstants();
        return Arrays.stream(enums)
                .filter(t->t.getOrdinal()==o)
                .findFirst()
                .orElseThrow(()->new EnumConstantNotPresentException(tClass, String.valueOf(o)));
    }
}
