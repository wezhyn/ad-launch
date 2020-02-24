package com.ad.admain.pay;

import com.alipay.api.response.AlipayFundTransUniTransferResponse;

import java.util.Objects;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
public class WithDrawResponse implements WithDrawNotification {

    public static final String SUCCESS_CODE="10000";
    private AlipayFundTransUniTransferResponse response;

    public WithDrawResponse(AlipayFundTransUniTransferResponse response) {
        this.response=response;
    }

    @Override
    public boolean isSuccess() {
        return Objects.equals(response.getCode(), SUCCESS_CODE);
    }

    @Override
    public String msg() {
        return response.getMsg();
    }

    @Override
    public String outBizNo() {
        if (!isSuccess()) {
            throw new RuntimeException("提现失败");
        }
        return response.getOutBizNo();
    }

    @Override
    public String tradeNo() {
        if (!isSuccess()) {
            throw new RuntimeException("提现失败");
        }
        return response.getOrderId();
    }

    @Override
    public WithDrawCode errCode() {
        if (isSuccess()) {
            return WithDrawCode.SUCCESS;
        }
        return WithDrawCode.valueOf(response.getSubCode());
    }
}
