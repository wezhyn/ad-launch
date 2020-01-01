package com.ad.admain.controller.pay;

import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 01.01.2020
 */
@AllArgsConstructor
public enum BillInfoSearchType implements StringEnum {


    /**
     * 搜索类型：根据订单状态
     */
    TRADE_STATUS("trade_status");

    private String searchType;

    @Override
    public String getValue() {
        return searchType;
    }
}
