package com.wezhyn.project.utils;

import com.wezhyn.project.annotation.UpdateIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * @author : zlb
 * @date : 2019/09/26
 */
@Slf4j
public class PropertyUtils {


    private static final Class<?>[] ANNOTATIONS={
            UpdateIgnore.class
    };

    /**
     * 将 source中的属性copy到target中，忽略 {@link PropertyUtils#getNullPropertyNames} 中存在的数据
     * 此处 是将web端传进来的数据与数据库端进行比较，将web端的source复制到从数据库中拿到的target，并
     * 忽略source中的部分属性
     *
     * @param source 数据源(请求跟新的数据:前端对象)
     * @param target 目标数据(即通过主键查询出来的数据：mysql端对象)
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }


    /**
     * 获取不参与 copy 的字段，默认 null值和包含 特定注解的不参与
     *
     * @param source 数据源(请求跟新的数据)
     * @return 不参与的字段
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src=new BeanWrapperImpl(source);
        PropertyDescriptor[] pds=src.getPropertyDescriptors();
        Set<String> emptyNames=new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String fieldName=propertyDescriptor.getName();
            Object srcValue=null;
            try {
                srcValue=src.getPropertyValue(fieldName);
            } catch (Exception ignore) {
            }
//            忽略 source中null值
            if (srcValue==null) {
                emptyNames.add(fieldName);
            } else {
                try {
//                忽视带有指定注解的字段
                    Field field=source.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    if (containsAnnotations(field)) {
                        emptyNames.add(fieldName);
                    }
//                    由spring保证
                } catch (NoSuchFieldException ignore) {
                }
            }
        }
        String[] result=new String[emptyNames.size()];
        if (log.isDebugEnabled()) {
            log.debug("{} ignore property: {}", source.getClass(), Arrays.toString(result));
        }
        return emptyNames.toArray(result);
    }

    /**
     * 检查是否包含特定注解
     *
     * @param field field
     * @return true:加入ignoreList
     */
    @SuppressWarnings("unchecked")
    private static boolean containsAnnotations(Field field) {
        for (Class ac : ANNOTATIONS) {
            if (field.isAnnotationPresent(ac)) {
                return true;
            }
        }
        return false;
    }
}
