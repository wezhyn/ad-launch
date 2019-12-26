package com.ad.admain.controller.pay;

import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 12.26.2019
 */
@AllArgsConstructor
public enum OrderSearchType implements StringEnum {

    /**
     * 搜索的类型
     */
    USER("user");

    private String type;

    @Override
    public String getValue() {
        return type;
    }
}
