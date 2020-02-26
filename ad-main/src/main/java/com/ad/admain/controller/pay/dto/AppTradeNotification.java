package com.ad.admain.controller.pay.dto;

import com.ad.admain.pay.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * 接受手机端发来的 APP 支付 通知信息
 * https://docs.open.alipay.com/204/105301/
 *
 * @author wezhyn
 * @since 12.01.2019
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppTradeNotification {
    /**
     * memo 是描述信息 (类型为字符串)
     */
    private String memo;

    /**
     * 当前系统订单号
     */
    private String outTradeNo;
    /**
     * 该交易在支付宝系统中的交易流水号
     */
    private String tradeNo;
    /**
     * 支付宝分配给开发者的应用 Id。
     */
    private String appId;
    private Double totalAmount;
    /**
     * 支付宝分配给开发者的应用 Id
     */
    private String sellerId;

    /**
     * 该方法支付宝执行状态
     */
    private String code;
    private String msg;

    /**
     * 订单状态
     */
    private TradeStatus tradeStatus;
    private Charset charset;
    private LocalDateTime timeStamp;
    private String sign;
    private String signType;

}
