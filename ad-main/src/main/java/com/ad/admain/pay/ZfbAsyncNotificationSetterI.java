package com.ad.admain.pay;

/**
 * 支付宝异步回调 Setter 接口
 *
 * @author wezhyn
 * @since 12.02.2019
 */
public interface ZfbAsyncNotificationSetterI {
    void setNotifyTime(String notifyTime);


    void setNotifyType(String notifyType);

    void setNotifyId(String notifyId);

    void setAppId(String appId);

    void setCharset(String charset);

    void setVersion(String version);

    void setSignType(String signType);

    void setSign(String sign);

    void setTradeNo(String tradeNo);

    void setOutTradeNo(String outTradeNo);

    void setOutBizNo(String outBizNo);

    void setBuyerId(String buyerId);

    void setBuyerLogonId(String buyerLogonId);

    void setSellerId(String sellerId);

    void setSellerEmail(String sellerEmail);

    void setTradeStatus(String tradeStatus);

    void setTotalAmount(String totalAmount);

    void setReceiptAmount(String receiptAmount);

    void setInvoiceAmount(String invoiceAmount);

    void setBuyerPayAmount(String buyerPayAmount);

    void setPointAmount(String pointAmount);

    void setRefundFee(String refundFee);

    void setSubject(String subject);

    void setBody(String body);

    void setGmtCreate(String gmtCreate);

    void setGmtPayment(String gmtPayment);

    void setGmtRefund(String gmtRefund);

    void setGmtClose(String gmtClose);

    void setFundBillList(String fundBillList);

    void setPassbackParams(String passbackParams);

    void setVoucherDetailList(String voucherDetailList);

}
