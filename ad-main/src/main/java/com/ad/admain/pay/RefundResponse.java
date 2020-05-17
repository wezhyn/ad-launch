package com.ad.admain.pay;

import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * @author wezhyn
 * @since 02.27.2020
 */
public class RefundResponse implements RefundNotification {

    private AlipayTradeRefundResponse response;


    public RefundResponse(AlipayTradeRefundResponse response) {
        this.response = response;
    }

    @Override
    public String getOutTradeNo() {
        return response.getTradeNo();
    }

    @Override
    public String getRefundCurrency() {
        return response.getRefundCurrency();
    }

    @Override
    public String getRefundFee() {
        return response.getRefundFee();
    }

    @Override
    public boolean isSuccess() {
        return this.response.isSuccess();
    }

    @Override
    public String err() {
        return this.response.getMsg();
    }
}
