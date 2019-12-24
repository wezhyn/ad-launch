package com.ad.admain.pay;

import com.ad.admain.controller.pay.TradeStatus;

/**
 * 订单处理接口
 *
 * @author wezhyn
 * @since 12.01.2019
 */
public interface ZfbTradeI {


    /**
     * 是否是本商城订单号: 并判断该订单的完成状态
     * 传递的参数不全有
     *
     * @param outTradeNo  支付宝异步通知参数
     * @param totalAmount 订单金额
     *                    https://docs.open.alipay.com/204/105301/
     * @return true: yes
     */
    TradeStatus checkOutTrade(String outTradeNo, Double totalAmount);


    /**
     * 处理当前订单状态,并附加一些额外信息
     *
     * @param alipayAsyncNotification 异步通知信息
     * @return true: 处理成功
     */
    boolean successNotificationAware(AlipayAsyncNotificationGetterI alipayAsyncNotification);

}
