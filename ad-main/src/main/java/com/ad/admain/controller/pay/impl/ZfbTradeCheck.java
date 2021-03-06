package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.BillInfoService;
import com.ad.admain.controller.pay.to.AdBillInfo;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.mq.order.PaymentOrderProduceI;
import com.ad.admain.pay.AlipayAsyncNotificationGetterI;
import com.ad.admain.pay.TradeStatus;
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
    private AdOrderService orderService;

    @Autowired
    private PaymentOrderProduceI paymentOrderProduce;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public TradeStatus checkOutTrade(String outTradeNo, Double totalAmount) {

        int tradeId=Integer.parseInt(outTradeNo);
        if (tradeId <= 0) {
            return TradeStatus.TRADE_CANCEL_OTHER;
        }
        Optional<AdBillInfo> orderInfo=orderInfoService.getByOrderId(tradeId);
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
//        修改订单状态并附加账单信息
        orderService.modifyOrderStatus(tradeId, OrderStatus.SUCCESS_PAYMENT);
        Optional<AdBillInfo> savedOrderInfo=orderInfoService.getByOrderId(tradeId);
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
