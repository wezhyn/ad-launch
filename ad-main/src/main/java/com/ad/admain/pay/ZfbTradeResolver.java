package com.ad.admain.pay;

import com.ad.admain.controller.pay.TradeStatus;

import java.util.Objects;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public class ZfbTradeResolver {

    /**
     * 支付宝反馈的字符串
     */
    private ZfbTradeI zfbTrade;

    public ZfbTradeResolver(ZfbTradeI zfbTrade) {
        this.zfbTrade=zfbTrade;
    }

    /**
     * 额外的的四部验签
     * https://docs.open.alipay.com/204/105301/ 四部验签
     * 1. 商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号
     * 2. 判断 total_amount 是否确实为该订单的实际金额 -> 当订单已经完成时
     * 3. 校验通知中的 seller_id
     * 4. 验证 app_id 是否为该商户本身
     *
     * @param zfbAsyncNotification 支付宝异步通知信息
     * @return 订单情况
     */
    public TradeStatus handleOrderStatus(AlipayAsyncNotificationGetterI zfbAsyncNotification) {
        TradeStatus orderStatus=zfbTrade.checkOutTrade(zfbAsyncNotification.getOutTradeNo(), zfbAsyncNotification.getTotalAmount());
//         支付成功或者支持异常都直接返回
        if (orderStatus==TradeStatus.TRADE_SUCCESS || orderStatus==TradeStatus.TRADE_CANCEL_OTHER) {
            return orderStatus;
        }
        return Objects.equals(zfbAsyncNotification.getAppId(), AliPayProperties.APP_ID)
                && Objects.equals(zfbAsyncNotification.getSellerId(), AliPayProperties.AD_SYSTEM_SELLER_ID) ?
                orderStatus : TradeStatus.TRADE_CANCEL_OTHER;
    }

    /**
     * 处理支付宝异步回调通知
     * 1. 四部验签
     * 2. 处理数据库
     *
     * @param alipayAsyncNotification
     * @return
     */
    public boolean handle(AlipayAsyncNotificationGetterI alipayAsyncNotification) {
        TradeStatus tradeStatus=handleOrderStatus(alipayAsyncNotification);
        if (tradeStatus==TradeStatus.TRADE_SUCCESS) {
            return true;
        } else if (tradeStatus==TradeStatus.TRADE_CANCEL_OTHER) {
            return false;
        }
        return zfbTrade.successNotificationAware(alipayAsyncNotification);
    }
}
