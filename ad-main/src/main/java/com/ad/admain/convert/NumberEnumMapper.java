package com.ad.admain.convert;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import com.wezhyn.project.utils.EnumUtils;
import org.mapstruct.TargetType;

/**
 * @author wezhyn
 * @since 02.26.2020
 */

public class NumberEnumMapper {
    /**
     * 用于在 {@link com.wezhyn.project.NumberEnum} 与 {@link Integer}
     * 之间进行转换
     *
     * @param value  {@link NumberEnum#getNumber()}
     * @param tClass 实现了 {@link StringEnum} 的子类
     * @param <T>    tClass
     * @return Enum<?>
     */
    public <T extends Enum<T> & NumberEnum> T numberEnum(Integer value, @TargetType Class<T> tClass) {
        if (value==null) {
            return null;
        }
        return EnumUtils.valueOfNumberEnum(tClass, value);
    }

    public <T extends Enum<?> & NumberEnum> Integer getNumberEnumValue(T numberEnum) {
        if (numberEnum==null) {
            return -1;
        }
        return numberEnum.getNumber();
    }
}
