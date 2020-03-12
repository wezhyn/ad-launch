package com.ad.launch.order;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;

/**
 * @author wezhyn
 * @since 12.24.2019
 */
public enum EquipmentVerify implements StringEnum, NumberEnum {
    /**
     * 设备状态
     */
    WAIT_VERITY(0, "wait"), PASSING_VERIFY(1, "pass"),
    FAILURE_VERIFY(-1, "fail"), MODIFY_VERIFY(-2, "modify");

    private Integer verifyNum;
    private String verify;

    EquipmentVerify(Integer verifyNum, String verify) {
        this.verifyNum=verifyNum;
        this.verify=verify;
    }

    @Override
    public Integer getNumber() {
        return verifyNum;
    }

    @Override
    public String getValue() {
        return verify;
    }
}
