package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.OrderSearchType;
import com.ad.admain.controller.pay.exception.OrderStatusException;
import com.ad.admain.controller.pay.repository.AdOrderRepository;
import com.ad.admain.controller.pay.repository.ProduceRepository;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
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
import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2020/2/24
 */
@Service
public class OrderServiceImpl extends AbstractBaseService<AdOrder, Integer> implements AdOrderService {

    private static final Page<AdOrder> EMPTY_ORDER_PAGE=new PageImpl<>(Collections.unmodifiableList(Collections.emptyList()));
    private ProduceRepository produceRepository;
    private AdOrderRepository adOrderRepository;

    @Autowired
    public OrderServiceImpl(AdOrderRepository adOrderRepository, ProduceRepository produceRepository) {
        this.adOrderRepository=adOrderRepository;
        this.produceRepository=produceRepository;
    }

    @Override
    public Optional<AdOrder> getById(Integer integer) {
        return adOrderRepository.findById(integer);
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<AdOrder> findUserOrder(Integer orderId, Integer userId) {
        AdOrder order=new AdOrder();
        order.setId(orderId);
        order.setUid(userId);
        return getRepository().findOne(Example.of(order));
    }


    @Override
    @Transactional(readOnly=true)
    public Page<AdOrder> listUserOrders(Integer userId, Pageable pageable) {
        return getRepository().findAdOrdersByUidOrderByIdDesc(userId, pageable);
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
