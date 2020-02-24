package com.ad.admain.controller.pay;

import com.wezhyn.project.NumberEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@AllArgsConstructor
public enum TradeType implements NumberEnum {


    /**
     * 交易的类型
     */
    AD(1), TRANSFER_ACCOUNT(2);
    private int type;

    @Override
    public Integer getNumber() {
        return type;
    }
}
