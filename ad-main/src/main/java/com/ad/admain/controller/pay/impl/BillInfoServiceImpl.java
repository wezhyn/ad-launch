package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.BillInfoSearchType;
import com.ad.admain.controller.pay.BillInfoService;
import com.ad.admain.controller.pay.TradeStatus;
import com.ad.admain.controller.pay.exception.SearchException;
import com.ad.admain.controller.pay.repository.BillInfoRepository;
import com.ad.admain.controller.pay.to.AdBillInfo;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.PayType;
import com.wezhyn.project.AbstractBaseService;
import com.wezhyn.project.utils.EnumUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
@Service
public class BillInfoServiceImpl extends AbstractBaseService<AdBillInfo, Integer> implements BillInfoService {

    private static final Page<AdBillInfo> EMPTY_BILL_PAGE=new PageImpl<AdBillInfo>(Collections.unmodifiableList(Collections.EMPTY_LIST));
    private final AdOrderService orderService;
    private final BillInfoRepository orderInfoRepository;


    public BillInfoServiceImpl(BillInfoRepository orderInfoRepository, AdOrderService orderService) {
        this.orderInfoRepository=orderInfoRepository;
        this.orderService=orderService;
    }


    @Override
    public BillInfoRepository getRepository() {
        return orderInfoRepository;
    }

    @Override
    public AdBillInfo createOrder(AdOrder order, PayType payType) {
        Order createdOrder=orderService.save(order);
        AdBillInfo orderInfo=AdBillInfo.builder()
                .orderId(createdOrder.getId())
                .totalAmount(order.getTotalAmount())
                .tradeStatus(TradeStatus.WAIT_BUYER_PAY)
                .build();
        return orderInfoRepository.save(orderInfo);
    }

    @Override
    public Optional<AdBillInfo> getByOrderId(Integer id) {
        return getRepository().findByOrderId(id);
    }

    @Override
    public Page<AdBillInfo> search(BillInfoSearchType type, String context, Pageable pageable) {
        AdBillInfo billInfo=new AdBillInfo();
        switch (type) {
            case TRADE_STATUS: {
                final TradeStatus tradeStatus=EnumUtils.valueOfStringEnumIgnoreCase(TradeStatus.class, context);
                billInfo.setTradeStatus(tradeStatus);
                break;
            }
            default: {
                throw new SearchException("无该搜索类型");
            }
        }
        Page<AdBillInfo> searchResult=getRepository().findAll(Example.of(billInfo), pageable);
        return searchResult.getSize()==0 ? EMPTY_BILL_PAGE : searchResult;
    }
}
