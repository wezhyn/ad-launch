package com.ad.adlaunch.utils;

import com.ad.adlaunch.enumate.BaseEnum;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public final class EnumUtils {


    /**
     * 返回 BaseEnum 子类中 value对应的枚举类
     *
     * @param tClass BaseEnum 的子类
     * @param name value
     * @param <T> T extends BaseEnum
     * @return BaseEnum
     */
    public static <T extends BaseEnum> T valueOfBaseEnum(Class<T> tClass, String name) {
        T[] enums=tClass.getEnumConstants();
        return Arrays.stream(enums)
                .filter(t->t.getValue().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(()->new RuntimeException("提供的参数无效 -> class: " + tClass.getSimpleName() + " name: " + name));
    }
    public static <T extends BaseEnum> T valueOfBaseEnum(Class<T> tClass, Integer o) {
        T[] enums=tClass.getEnumConstants();
        return Arrays.stream(enums)
                .filter(t->t.getOrdinal()==o)
                .findFirst()
                .orElseThrow(()->new RuntimeException("提供的参数无效 -> class: " + tClass.getSimpleName() + " o: " +o ));
    }
}
