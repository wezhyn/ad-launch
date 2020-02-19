package com.ad.admain.controller.dashboard;

import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 01.22.2020
 */
@AllArgsConstructor
public enum DateType implements StringEnum {

    /**
     * 日期类型
     */
    HOUR, DAY, WEEK, MONTH, YEAR;


    @Override
    public String getValue() {
        return name().toLowerCase();
    }
}
