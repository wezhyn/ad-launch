package com.ad.admain.controller.pay.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@AllArgsConstructor
@Builder
@Getter
public class RefundOrder extends Order {


    /**
     * out_trade_no
     */
    private Integer adOrderId;

    private String tradeNo;

    private String refundCurrency;

    private String refundReason;

    private String operatorId;


}
