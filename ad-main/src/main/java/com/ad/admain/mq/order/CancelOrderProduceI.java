package com.ad.admain.mq.order;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
public interface CancelOrderProduceI {


    /**
     * 发送取消订单信息
     *
     * @param message id,uid
     */
    void cancelOrder(final CancelOrderMessage message);
}
