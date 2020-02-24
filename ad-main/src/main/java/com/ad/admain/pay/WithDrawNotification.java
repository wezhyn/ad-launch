package com.ad.admain.pay;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
public interface WithDrawNotification {

    /**
     * 提现是否成功
     *
     * @return true:成功
     */
    boolean isSuccess();

    /**
     * 响应结果
     *
     * @return 成功|错误 信息
     */
    String msg();

    /**
     * 成功后返回的信息
     *
     * @return 本系统订单号
     */
    String outBizNo();

    /**
     * 成功后返回的信息
     * 支付宝订单号
     *
     * @return zfb
     */
    String tradeNo();

    /**
     * 公共错误码
     *
     * @return 错误码
     */
    WithDrawCode errCode();

}
