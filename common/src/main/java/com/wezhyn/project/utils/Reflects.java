package com.wezhyn.project.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wezhyn
 * @since 12.02.2019
 */
public final class Reflects {

    private static final List<Class<?>> IGNORE_ANNOTATION=new ArrayList<>(6);

    static {
        IGNORE_ANNOTATION.add(Target.class);
        IGNORE_ANNOTATION.add(Retention.class);
        IGNORE_ANNOTATION.add(Inherited.class);
        IGNORE_ANNOTATION.add(Documented.class);
    }

    public static void sortConstructors(Constructor<?>[] constructors) {
        Arrays.sort(constructors, (c1, c2)->{
//          包可见:0;  public:1 ;private: 2 protected: 4
//            public :true
            boolean c1IsPublic=Modifier.isPublic(c1.getModifiers());
            boolean c2IsPublic=Modifier.isPublic(c2.getModifiers());
            if (c1IsPublic!=c2IsPublic) {
                return c1IsPublic ? -1 : 1;
            }
            int c1ParamCount=c1.getParameterCount();
            int c2ParamCount=c2.getParameterCount();
//          少构造参数优先 : 1
            return Integer.compare(c2ParamCount, c1ParamCount);
        });

    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> klass) {
        Constructor<T>[] cons=(Constructor<T>[]) klass.getDeclaredConstructors();
        if (cons.length==0) {
            return null;
        }
//        发现可见级最大和构造参数最少的构造器
        Reflects.sortConstructors(cons);
        Constructor<T> primaryConstructor=cons[0];
        int populateNullParamsLength=primaryConstructor.getParameterCount();
        Object[] populateParams=new Object[populateNullParamsLength];
        Arrays.fill(populateParams, null);
        try {
            return primaryConstructor.newInstance(populateParams);
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("无法通过构造器创建类");
        }
    }

    public static void setField(Field field, @Nullable Object target, @Nullable Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 发现标注在其他注解上的注解
     *
     * @param field              查询的字段
     * @param selectedAnnotation 要查询的注解
     * @param <T>                类型
     * @return Annotation
     */
    public static <T extends Annotation> Optional<T> getMetaAnnotation(Field field, Class<T> selectedAnnotation) {
        Assert.notNull(field, "查询的字段为空");
        T annotation=field.getAnnotation(selectedAnnotation);

//        类上有该注解
        if (annotation!=null) {
            return Optional.of(annotation);
        }
//        查询类似 @Body 上的 @Selector 元注解
        Annotation[] declaredAnnotations=field.getDeclaredAnnotations();
        Queue<Annotation> searchAnnotation=new LinkedBlockingQueue<>(Arrays.asList(declaredAnnotations));
        while (searchAnnotation.peek()!=null) {
            Class<? extends Annotation> annotationType=searchAnnotation.poll().annotationType();
            if (annotationType.isAnnotationPresent(selectedAnnotation)) {
                return Optional.of(annotationType.getAnnotation(selectedAnnotation));
            }
//            将当前注解的元注解放入搜索队列中
            Annotation[] parentAnnotations=annotationType.getDeclaredAnnotations();
            for (Annotation parentAnnotation : parentAnnotations) {
                Class<? extends Annotation> parentType=parentAnnotation.annotationType();
                if (!IGNORE_ANNOTATION.contains(parentType)) {
                    searchAnnotation.add(parentAnnotation);
                }
            }
        }

        return Optional.empty();
    }
}
