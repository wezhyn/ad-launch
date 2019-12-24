package com.ad.admain.controller.equipment.entity;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 12.24.2019
 */
@AllArgsConstructor
public enum EquipmentVerify implements StringEnum, NumberEnum {
    /**
     * 设备状态
     */
    WAIT_VERITY(0, "wait"), PASSING_VERIFY(1, "pass"),
    FAILURE_VERIFY(-1, "fail"), MODIFY_VERIFY(-2, "modify");

    private Integer verifyNum;
    private String verify;

    @Override
    public Integer getNumber() {
        return verifyNum;
    }

    @Override
    public String getValue() {
        return verify;
    }
}
