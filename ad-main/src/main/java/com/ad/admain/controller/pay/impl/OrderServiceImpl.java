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

import java.util.Collections;
import java.util.List;
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
    @Transactional(rollbackFor=Exception.class)
    public Optional<AdOrder> trySuccessOrder(Integer orderId, Integer uid) {
        final Optional<AdOrder> userOrder=findUserOrder(orderId, uid);
        if (userOrder.isPresent()) {
            AdOrder o=userOrder.get();
            switch (o.getOrderStatus()) {
                case WAITING_PAYMENT: {
                    getRepository().updateOrderStatus(orderId, uid, OrderStatus.WAITING_PAYMENT, OrderStatus.CANCEL);
                    return Optional.empty();
                }
                case SUCCESS_PAYMENT: {
                    getRepository().updateOrderStatus(orderId, uid, OrderStatus.SUCCESS_PAYMENT, OrderStatus.EXECUTING);
                    return userOrder;
                }
                default: {
                    return Optional.empty();
                }
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Boolean isUserOrder(Integer orderId, Integer userId) {
        return getRepository().existsByIdAndUid(orderId, userId);
    }

    @Override
    @Transactional(readOnly=true)
    public Page<AdOrder> listUserOrders(Integer userId, Pageable pageable) {
        return getRepository().findAdOrdersByUidAndIsDeleteIsFalse(userId, pageable);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean modifyOrderStatus(Integer orderId, OrderStatus orderStatus) {
        final Optional<AdOrder> one=getRepository().findById(orderId);
        if (!one.isPresent()) {
            throw new OrderStatusException("无该订单信息");
        }
        AdOrder order=one.get();
        OrderStatus currentStatus=order.getOrderStatus();
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
            return false;
        } else if (nextStatus==null) {
            throw new OrderStatusException("无法越级更新订单状态");
        } else {
            return getRepository().updateOrderStatus(orderId, currentStatus, nextStatus) > 0;
        }
    }

    @Override
    public AdOrderRepository getRepository() {
        return adOrderRepository;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean rollbackRefundOrderStatus(Integer orderId, OrderStatus originStatus) {
        return getRepository().updateOrderStatus(orderId, OrderStatus.REFUNDING, originStatus) > 0;
    }

    @Override
    public AdOrder findById(Integer id) {
        return adOrderRepository.findById(id).orElse(null);
    }

    @Override
    public Integer updateExecuted(Integer oid, Integer executed) {
        return adOrderRepository.updateExecuted(oid, executed);
    }

    @Override
    public List<AdOrder> findByEnum(Integer type) {
        OrderStatus orderStatus=null;
        switch (type) {
            case 3: {
                orderStatus=OrderStatus.EXECUTING;
                break;
            }
        }
        List<AdOrder> adOrders=adOrderRepository.findAdOrdersByOrderStatusEquals(orderStatus);
        if (adOrders!=null && adOrders.size()!=0) {
            return adOrders;
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public void delete(Integer integer) {
        AdOrder order=new AdOrder();
        order.setId(integer);
        order.setIsDelete(true);
        update(order);
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
