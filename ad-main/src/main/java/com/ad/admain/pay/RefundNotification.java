package com.ad.admain.pay;

/**
 * @author wezhyn
 * @since 02.27.2020
 */
public interface RefundNotification {
    /**
     * 退款是否成功
     *
     * @return true:成功
     */
    boolean isSuccess();


    String err();
}
