package com.wezhyn.project.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author wezhyn
 * @since 12.02.2019
 */
public final class Reflects {


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

}
