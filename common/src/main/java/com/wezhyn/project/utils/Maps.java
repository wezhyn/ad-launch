package com.wezhyn.project.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author wezhyn
 * @since 12.02.2019
 */
@Slf4j
public final class Maps {


    /**
     * 通过将Map中的 property 注入到相应的类中
     * 会将map 中的 full_name key 转化为 fullName , fullName 则还是fullName
     *
     * @param readMap       map
     * @param populateClass 需要创建的实例
     * @param <T>           类型
     * @return newInstance
     */
    public static <T> T populateBean(Map<String, String> readMap, Class<T> populateClass) {
        if (readMap==null || readMap.size()==0) {
            return null;
        }
        T newInstant=Reflects.newInstance(populateClass);
        for (String s : readMap.keySet()) {
            try {
                Field field=populateClass.getDeclaredField(Strings.underlineNameToPropertyName(s));
                field.setAccessible(true);
                field.set(newInstant, Maps.getValueIgnoreError(readMap, s));
            } catch (NoSuchFieldException e) {
                log.error("notSuchField : {} for Class {}", s, populateClass);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("非法反射注入实体类");
            }
        }
        return newInstant;
    }


    /**
     * 忽略获取中的异常
     *
     * @param readyMap  map
     * @param paramName key
     * @param <T>       type
     * @return value
     */
    public static <T> T getValueIgnoreError(Map<String, T> readyMap, String paramName) {
        try {
            return readyMap.get(paramName);
        } catch (NullPointerException ignore) {
            return null;
        }
    }
}
