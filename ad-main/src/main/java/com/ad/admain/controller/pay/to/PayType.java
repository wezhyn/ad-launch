package com.ad.admain.controller.pay.to;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 01.01.2020
 */
@AllArgsConstructor
public enum PayType implements StringEnum, NumberEnum {

    /**
     * 支付的类型
     */
    ALI_PAY(0, "alipay"), WECHAT(1, "wechat");

    private Integer payNum;
    private String payName;


    @Override
    public Integer getNumber() {
        return payNum;
    }

    @Override
    public String getValue() {
        return payName;
    }


}
