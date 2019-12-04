package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.OrderInfoService;
import com.ad.admain.controller.pay.TradeStatus;
import com.ad.admain.controller.pay.repository.OrderInfoRepository;
import com.ad.admain.controller.pay.to.OrderInfo;
import com.ad.admain.pay.AlipayAsyncNotificationGetterI;
import com.ad.admain.pay.ZfbTradeI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
@Component
public class ZfbTradeCheck implements ZfbTradeI {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public TradeStatus checkOutTrade(String outTradeNo, Double totalAmount) {

        int tradeId=Integer.parseInt(outTradeNo);
        if (tradeId <= 0) {
            return TradeStatus.TRADE_CANCEL_OTHER;
        }
        Optional<OrderInfo> orderInfo=orderInfoRepository.findById(tradeId);
        return orderInfo.map(o->{
            Double savedAmount=o.getTotalAmount();
            TradeStatus savedStatus=o.getTradeStatus();
            return savedStatus==TradeStatus.TRADE_SUCCESS ? savedStatus :
                    (Objects.equals(totalAmount, savedAmount) ? savedStatus : TradeStatus.TRADE_CANCEL_OTHER);
        }).orElse(TradeStatus.TRADE_NETWORK_FAILURE);

    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean successNotificationAware(AlipayAsyncNotificationGetterI alipayAsyncNotification) {
        int tradeId=Integer.parseInt(alipayAsyncNotification.getOutTradeNo());
        Optional<OrderInfo> savedOrderInfo=orderInfoRepository.findById(tradeId);
        if (savedOrderInfo.isPresent()) {
            OrderInfo oInfo=savedOrderInfo.get();
            oInfo.setTradeStatus(TradeStatus.TRADE_SUCCESS);
            oInfo.setBuyerId(alipayAsyncNotification.getBuyerId());
            oInfo.setSellerId(alipayAsyncNotification.getSellerId());
            oInfo.setAlipayTradeNo(alipayAsyncNotification.getTradeNo());
            oInfo.setGmtPayment(alipayAsyncNotification.getPayment());
            orderInfoService.update(oInfo);
            return true;
        }
        return false;
    }
}
