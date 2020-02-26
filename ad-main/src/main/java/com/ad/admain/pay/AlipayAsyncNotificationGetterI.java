package com.ad.admain.pay;

import java.time.LocalDateTime;

/**
 * 支付宝异步通知参数的 Getter 接口
 *
 * @author wezhyn
 * @since 12.01.2019
 */
public interface AlipayAsyncNotificationGetterI {

    Boolean isVerify();

    /**
     * 通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return time
     */
    LocalDateTime getNotifyTime();

    LocalDateTime getGmtCreate();

    LocalDateTime getGmtPayment();

    /**
     * 通知的类型
     *
     * @return trade_status_sync
     */
    String getNotifyType();

    /**
     * 编码格式
     *
     * @return utf-8
     */
    String getCharset();

    /**
     * 异步返回结果的验签
     *
     * @return 601510b7970e52cc63db0f44997cf70e
     */
    String getSign();

    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2
     *
     * @return RSA2
     */
    String getSignType();

    String getBuyerId();

    /**
     * 原支付请求的商户订单号
     *
     * @return 6823789339978248
     */
    String getOutTradeNo();

    /**
     * 支付宝交易凭证号
     *
     * @return 2013112011001004330000121536
     */
    String getTradeNo();

    /**
     * 商户业务 ID，主要是退款通知中返回退款申请的流水号 可选
     *
     * @return HZRF001
     */
    String getOutBizNo();


    /**
     * 支付宝分配给开发者的应用 Id
     *
     * @return 2014072300007148
     */
    String getAppId();

    /**
     * 通知校验 ID
     * 支付宝发送同一条异步通知时（包含商户并未成功打印出 success 导致支付宝重发数次通知），
     * 服务器异步通知参数 notify_id 是不变的
     *
     * @return ac05099524730693a8b330c5ecf72da9786
     */
    String getNotifyId();

    /**
     * 本次交易支付的订单金额，单位为人民币（元）
     *
     * @return total_amount
     */
    Double getTotalAmount();

    /**
     * 交易目前所处的状态 可选
     *
     * @return TRADE_CLOSED
     */
    TradeStatus getTradeStatus();

    /**
     * 卖家支付宝用户号
     *
     * @return 2088101106499364
     */
    String getSellerId();
}
