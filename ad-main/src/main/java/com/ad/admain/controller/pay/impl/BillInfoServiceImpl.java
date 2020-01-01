package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.BillInfoSearchType;
import com.ad.admain.controller.pay.BillInfoService;
import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.TradeStatus;
import com.ad.admain.controller.pay.exception.SearchException;
import com.ad.admain.controller.pay.repository.BillInfoRepository;
import com.ad.admain.controller.pay.to.BillInfo;
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
public class BillInfoServiceImpl extends AbstractBaseService<BillInfo, Integer> implements BillInfoService {

    private static final Page<BillInfo> EMPTY_BILL_PAGE=new PageImpl<BillInfo>(Collections.unmodifiableList(Collections.EMPTY_LIST));
    private final OrderService orderService;
    private final BillInfoRepository orderInfoRepository;


    public BillInfoServiceImpl(BillInfoRepository orderInfoRepository, OrderService orderService) {
        this.orderInfoRepository=orderInfoRepository;
        this.orderService=orderService;
    }


    @Override
    public BillInfoRepository getRepository() {
        return orderInfoRepository;
    }

    @Override
    public BillInfo createOrder(Order order, PayType payType) {
        Order createdOrder=orderService.save(order);
        BillInfo orderInfo=BillInfo.builder()
                .orderId(createdOrder.getId())
                .totalAmount(order.getPrice()*order.getNum())
                .tradeStatus(TradeStatus.WAIT_BUYER_PAY)
                .build();
        return orderInfoRepository.save(orderInfo);
    }

    @Override
    public Optional<BillInfo> getByOrderId(Integer id) {
        return getRepository().findByOrderId(id);
    }

    @Override
    public Page<BillInfo> search(BillInfoSearchType type, String context, Pageable pageable) {
        BillInfo billInfo=new BillInfo();
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
        Page<BillInfo> searchResult=getRepository().findAll(Example.of(billInfo), pageable);
        return searchResult.getSize()==0 ? EMPTY_BILL_PAGE : searchResult;
    }
}
