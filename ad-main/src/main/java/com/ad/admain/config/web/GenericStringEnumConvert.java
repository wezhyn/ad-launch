package com.ad.admain.config.web;

import com.ad.admain.common.BaseEnum;
import com.ad.admain.enumate.StringEnum;
import com.ad.admain.utils.EnumUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.StringUtils;

/**
 * 支持序列化以下类实现{@link String} -> @{@link StringEnum}
 * 用于 SpringMvc
 * {@link org.springframework.web.bind.annotation.PathVariable}
 * {@link org.springframework.web.bind.annotation.RequestParam}
 *
 * @author wezhyn
 * @date 3/19/19 : 3:55 PM
 */
public class GenericStringEnumConvert implements ConverterFactory<String, StringEnum> {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends StringEnum> Converter<String, T> getConverter(Class<T> aClass) {

        Class<?> enumType=aClass;
        while (enumType!=null && !enumType.isEnum()) {
            enumType=enumType.getSuperclass();
        }
        return new StringToBaseEnum(enumType);
    }

    private static class StringToBaseEnum<T extends Enum & BaseEnum> implements Converter<String, T> {

        private final Class<T> enumType;

        private StringToBaseEnum(Class<T> enumType) {
            this.enumType=enumType;
        }

        @Override
        public T convert(String s) {
            if (StringUtils.isEmpty(s)) {
                return null;
            }
            return EnumUtils.valueOfBaseEnumIgnoreCase(enumType, s);
        }
    }
}
