package com.ad.admain.controller.pay.to;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 12.24.2019
 */
@AllArgsConstructor
public enum OrderVerify implements StringEnum, NumberEnum {
    /**
     * 订单审核状态
     * 代客服审核
     * 0: 代审核
     * 1:审核通过
     * -1：审核不通过
     * -2：订单代修改
     */
    WAIT_VERITY(0, "wait"), PASSING_VERIFY(1, "pass"),
    FAILURE_VERIFY(-1, "failure"), MODIFY_VERIFY(-2, "modify");

    private Integer verifyNum;
    private String verify;

    @Override
    public String getValue() {
        return verify;
    }

    @Override
    public Integer getNumber() {
        return verifyNum;
    }
}
