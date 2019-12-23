package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.OrderInfoService;
import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.TradeStatus;
import com.ad.admain.controller.pay.repository.OrderInfoRepository;
import com.ad.admain.controller.pay.to.BillInfo;
import com.ad.admain.controller.pay.to.Order;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
@Service
public class OrderInfoServiceImpl extends AbstractBaseService<BillInfo, Integer> implements OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;
    private final OrderService orderService;

    public OrderInfoServiceImpl(OrderInfoRepository orderInfoRepository, OrderService orderService) {
        this.orderInfoRepository=orderInfoRepository;
        this.orderService=orderService;
    }


    @Override
    public OrderInfoRepository getRepository() {
        return orderInfoRepository;
    }

    @Override
    public Optional<BillInfo> createOrder(Order order) {
        Optional<Order> createdOrder=orderService.save(order);
        return createdOrder.map(o->{
            BillInfo orderInfo=BillInfo.builder()
                    .orderId(order.getId())
                    .totalAmount(order.getPrice()*order.getNum())
                    .tradeStatus(TradeStatus.WAIT_BUYER_PAY)
                    .build();
            return orderInfoRepository.save(orderInfo);
        });
    }
}
