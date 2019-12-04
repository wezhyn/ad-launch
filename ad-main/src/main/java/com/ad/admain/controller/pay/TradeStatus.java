package com.ad.admain.controller.pay;

import com.ad.admain.common.BaseEnum;
import lombok.AllArgsConstructor;

/**
 * 订单状态
 *
 * @author wezhyn
 * @since 12.01.2019
 */
@AllArgsConstructor
public enum TradeStatus implements BaseEnum {


    /**
     * 交易创建
     */
    WAIT_BUYER_PAY(1000, "交易创建，等待买家付款"),
    /**
     * 正在处理中，
     * 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     */
    TRADE_HANDLE(8000, "正在处理中"),
    /**
     * 交易支付成功
     */
    TRADE_SUCCESS(9000, "订单支付成功"),
    TRADE_FINISHED(-9000, "交易结束"),


    /**
     * 支付状态异常
     */
    TRADE_CLOSED(-1000, "未付款交易超时关闭，或支付完成后全额退款"),
    TRADE_CANCEL_OTHER(-6000, "其它支付错误"),

    /**
     * 支付异常
     */
    TRADE_FAILURE(4000, "订单支付失败"),
    TRADE_REPEAT(5000, "重复请求"),
    TRADE_CANCEL_USER(6001, "中途取消交易"),
    TRADE_NETWORK_FAILURE(6002, "网络连接出错"),
    /**
     * 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     */
    TRADE_CANCEL_UNKNOWN(6004, "支付结果未知(有可能已经支付成功)"),

    /**
     * 订单退款
     */
    TRADE_Refunding(0, "订单正在退款中"),
    TRADE_Refunded(1, "订单退款完成");

    private Integer tradeStatus;
    private String tradeMessage;

    @Override
    public int getOrdinal() {
        return tradeStatus;
    }

    @Override
    public String getValue() {
        return tradeMessage;
    }
}
