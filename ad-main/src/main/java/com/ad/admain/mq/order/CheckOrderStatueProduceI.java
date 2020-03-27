package com.ad.admain.mq.order;

/**
 * @author wezhyn
 * @since 02.29.2020
 */
public interface CheckOrderStatueProduceI {


    /**
     * 发送检查订单信息
     *
     * @param message id,uid
     */
    void checkOrder(final CheckOrderMessage message);
}
