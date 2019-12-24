package com.wezhyn.project;


import com.wezhyn.project.converter.TypeConverter;
import com.wezhyn.project.utils.Maps;
import com.wezhyn.project.utils.Strings;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础的 Model ,转化至 T 类
 * todo: 改成Cglib 实现
 *
 * @author wezhyn
 * @since 12.02.2019
 */
public abstract class AbstractBaseMapAdapterModel<T> implements BaseModelI {

    private Map<String, String> prepareMap;
    /**
     * 具体 getter 或 setter 的声明接口
     */
    private Class<?>[] interfaces;
    private Class<T> currentClass;


    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Map<String, String> prepareMap, Class<T> populateKlass, boolean isUnderline) {
        if (populateKlass.isInterface()) {
            return (T) Enhancer.create(Object.class, new Class[]{populateKlass}, new MapAdapterCallbackInterceptor(prepareMap, isUnderline));
        }
        return Maps.populateBean(prepareMap, populateKlass);
    }


    private static class MapAdapterCallbackInterceptor implements MethodInterceptor {

        private static final List<TypeConverter<?, ?>> typeConverters;

        static {
            typeConverters=new ArrayList<>();
            typeConverters.add(new LocalDateTimeConverter());
            typeConverters.add(new DoubleConverter());
            typeConverters.add(new BooleanConverter());
        }

        private Map<String, String> parseMap;
        private boolean isUnderline;

        public MapAdapterCallbackInterceptor(Map<String, String> parseMap, boolean isUnderline) {
            this.parseMap=parseMap;
            this.isUnderline=isUnderline;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            Class<?> returnType=method.getReturnType();
            String propertyName=isUnderline ? Strings.extractGetterAndSetterUnderlineProperty(method.getName())
                    : Strings.extractGetterAndSetterProperty(method.getName());
            String mapValue=Maps.getValueIgnoreError(parseMap, propertyName);
            for (TypeConverter typeConverter : typeConverters) {
                if (typeConverter.canConvert(returnType)) {
                    return typeConverter.convert(mapValue, returnType);
                }
            }
            return mapValue;
        }
    }


    private static class BooleanConverter implements TypeConverter<String, Boolean> {

        @Override
        public boolean canConvert(Class<?> targetType) {
            return Boolean.class.isAssignableFrom(targetType);
        }

        @Override
        public Boolean convert(String source, Class<Boolean> targetType) {
            return Boolean.parseBoolean(source);
        }
    }

    private static class DoubleConverter implements TypeConverter<String, Double> {

        @Override
        public boolean canConvert(Class<?> targetType) {
            return Double.class.isAssignableFrom(targetType);
        }

        @Override
        public Double convert(String source, Class<Double> targetType) {
            return Double.parseDouble(source);
        }
    }

    private static class LocalDateTimeConverter implements TypeConverter<String, LocalDateTime> {
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public boolean canConvert(Class<?> targetType) {
            return LocalDateTime.class.isAssignableFrom(targetType);
        }

        @Override
        public LocalDateTime convert(String source, Class<LocalDateTime> targetType) {
            return LocalDateTime.parse(source, dateTimeFormatter);
        }
    }


    /**
     * 使用数据已经填好的map
     *
     * @param prepareMap map
     */
/*    public AbstractBaseMapAdapterModel(Map<String, String> prepareMap, Class<?>[] interfaces) {
        this.prepareMap=prepareMap;
        this.interfaces=interfaces;
        this.currentClass=(Class<T>) this.getClass();
    }*/

    /**
     * 使用数据已经填好的map
     *
     * @param prepareMap map
     */
/*    public AbstractBaseMapAdapterModel(Map<String, String> prepareMap, Class<?> inter) {
        this(prepareMap, new Class[]{inter});
    }*/

    /**
     * 相应 Map 对应的实际对象
     *
     * @param t 订单
     */
/*    public <T> T create(T t) {
//        Enhancer.create();
        return null;
    }*/

}
