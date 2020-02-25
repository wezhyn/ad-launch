package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.OrderSearchType;
import com.ad.admain.controller.pay.exception.OrderStatusException;
import com.ad.admain.controller.pay.repository.AdOrderRepository;
import com.ad.admain.controller.pay.repository.ValueReposity;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.controller.pay.to.Value;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2020/2/24
 */
@Service
public class OrderServiceImpl extends AbstractBaseService<AdOrder, Integer> implements AdOrderService {

    private static final Page<AdOrder> EMPTY_ORDER_PAGE=new PageImpl<>(Collections.unmodifiableList(Collections.emptyList()));
    private ValueReposity valueReposity;
    private AdOrderRepository adOrderRepository;

    @Autowired
    public OrderServiceImpl(AdOrderRepository adOrderRepository, ValueReposity valueReposity) {
        this.adOrderRepository=adOrderRepository;
        this.valueReposity=valueReposity;
    }

    @Override
    public Optional<AdOrder> getById(Integer integer) {
        return adOrderRepository.findById(integer);
    }

    @Override
    public AdOrder save(AdOrder object) {
        Assert.notNull(object.getPrice(), "无单价");
        Assert.notNull(object.getNum(), "无商品数量");
        object.setTotalAmount(object.getPrice()*object.getNum());
        AdOrder order=adOrderRepository.save(object);
        List<Value> values=order.getValueList();
        Iterator<Value> iterator=values.iterator();
        while (iterator.hasNext()) {
            Value val=iterator.next();
            valueReposity.save(val);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void modifyOrderStatus(Integer orderId, OrderStatus orderStatus) {
        final AdOrder one=getRepository().getOne(orderId);
        Assert.notNull(one, "无目标订单");
        OrderStatus currentStatus=one.getOrderStatus();
        OrderStatus nextStatus=null;
        if (orderStatus.getNumber()==currentStatus.getNumber() + 1) {
//            正常升级
            nextStatus=orderStatus;
        } else if (orderStatus.getNumber() < 0) {
            switch (orderStatus) {
                case CANCEL: {
                    if (currentStatus!=OrderStatus.WAITING_PAYMENT) {
                        throw new OrderStatusException("订单已经成功付款，无法取消");
                    }
                    nextStatus=OrderStatus.WAITING_PAYMENT;
                    break;
                }
                case REFUNDING: {
                    if (currentStatus.getNumber() > 0 && currentStatus.getNumber() < OrderStatus.EXECUTION_COMPLETED.getNumber()) {
                        nextStatus=OrderStatus.REFUNDING;
                    }
                    break;
                }
                default: {
                    break;
                }
            }

        }
        if (orderStatus==currentStatus) {
        } else if (nextStatus==null) {
            throw new OrderStatusException("无法越级更新订单状态");
        } else {
            getRepository().updateOrderStatus(orderId, currentStatus, nextStatus);
        }
    }

    @Override
    public AdOrderRepository getRepository() {
        return adOrderRepository;
    }

    @Override
    public Page<AdOrder> search(OrderSearchType type, String context, Pageable pageable) {
        AdOrder searchOrder=new AdOrder();
        switch (type) {
            case USER: {
                Integer userId=Integer.parseInt(context);
                searchOrder.setUid(userId);
                break;
            }
            default: {
                return EMPTY_ORDER_PAGE;
            }
        }
        Page<AdOrder> searchResult=getRepository().findAll(Example.of(searchOrder), pageable);
        return searchResult.getSize()==0 ? EMPTY_ORDER_PAGE : searchResult;
    }
}
