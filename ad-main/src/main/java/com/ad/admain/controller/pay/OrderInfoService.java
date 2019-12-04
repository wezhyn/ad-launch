package com.ad.admain.controller.pay;

import com.ad.admain.common.BaseService;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.OrderInfo;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public interface OrderInfoService extends BaseService<OrderInfo, Integer> {


    /**
     * 创建订单信息
     *
     * @param order order
     * @return orderInfo
     */
    Optional<OrderInfo> createOrder(Order order);
}
