package com.ad.adlaunch.convert;

import com.ad.adlaunch.enumate.BaseEnum;
import org.hibernate.annotations.common.util.StandardClassLoaderDelegateImpl;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * 适用于一类
 *
 * @param <T>
 */
//@TypeDef(name="enu", typeClass=BaseEnum.class)
public class BaseEnumTypes<T extends BaseEnum> implements DynamicParameterizedType {

    private static final String ENUM="enumClass";
    private Class<T> enumClass;

    /**
     * 在此处访问一些动态参数
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader=(ParameterType) parameters.get(PARAMETER_TYPE);
        if (reader!=null) {
            enumClass=reader.getReturnedClass().asSubclass(Enum.class);
        } else {
            final String enumClassName=(String) parameters.get(ENUM);
            enumClass=StandardClassLoaderDelegateImpl.INSTANCE.classForName(enumClassName);
        }
    }


}
