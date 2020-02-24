package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.BillInfoService;
import com.ad.admain.controller.pay.TradeStatus;
import com.ad.admain.controller.pay.repository.BillInfoRepository;
import com.ad.admain.controller.pay.to.AdBillInfo;
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
    private BillInfoService orderInfoService;

    @Autowired
    private BillInfoRepository orderInfoRepository;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public TradeStatus checkOutTrade(String outTradeNo, Double totalAmount) {

        int tradeId=Integer.parseInt(outTradeNo);
        if (tradeId <= 0) {
            return TradeStatus.TRADE_CANCEL_OTHER;
        }
        Optional<AdBillInfo> orderInfo=orderInfoRepository.findByOrderId(tradeId);
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
        Optional<AdBillInfo> savedOrderInfo=orderInfoRepository.findByOrderId(tradeId);
        if (savedOrderInfo.isPresent()) {
            AdBillInfo oInfo=savedOrderInfo.get();
            oInfo.setTradeStatus(TradeStatus.TRADE_SUCCESS);
            oInfo.setBuyerId(alipayAsyncNotification.getBuyerId());
            oInfo.setSellerId(alipayAsyncNotification.getSellerId());
            oInfo.setAlipayTradeNo(alipayAsyncNotification.getTradeNo());
            oInfo.setGmtPayment(alipayAsyncNotification.getGmtPayment());
            oInfo.setGmtCreate(alipayAsyncNotification.getGmtCreate());
            orderInfoService.save(oInfo);
            return true;
        }
        return false;
    }
}
