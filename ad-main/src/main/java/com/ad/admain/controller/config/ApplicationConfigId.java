package com.ad.admain.controller.config;

import com.wezhyn.project.NumberEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Getter
@AllArgsConstructor
public enum ApplicationConfigId implements NumberEnum {

    /**
     * 种类
     */
    REVENUE(1);

    private final int id;

    @Override
    public Integer getNumber() {
        return id;
    }
}
