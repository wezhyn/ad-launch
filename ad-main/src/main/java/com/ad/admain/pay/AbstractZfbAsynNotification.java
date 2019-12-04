package com.ad.admain.pay;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wezhyn
 * @since 12.03.2019
 */
@Setter
@Getter
public abstract class AbstractZfbAsynNotification {
    private String notifyTime;
    private String notifyType;
    private String notifyId;
    private String appId;
    private String charset;
    private String version;
    private String signType;
    private String sign;
    private String tradeNo;
    private String outTradeNo;
    private String outBizNo;
    private String buyerId;
    private String buyerLogonId;
    private String sellerId;
    private String sellerEmail;
    private String tradeStatus;
    private String totalAmount;
    private String receiptAmount;
    private String invoiceAmount;
    private String buyerPayAmount;
    private String pointAmount;
    private String refundFee;
    private String subject;
    private String body;
    private String gmtCreate;
    private String gmtPayment;
    private String gmtRefund;
    private String gmtClose;
    private String fundBillList;
    private String passbackParams;
    private String voucherDetailList;
}
