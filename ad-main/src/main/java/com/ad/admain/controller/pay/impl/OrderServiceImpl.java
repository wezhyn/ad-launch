package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.OrderSearchType;
import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.repository.OrderReposity;
import com.ad.admain.controller.pay.repository.ValueReposity;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.Value;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl extends AbstractBaseService<Order, Integer> implements OrderService {

    private OrderReposity orderReposity;
    private ValueReposity valueReposity;
    private static final Page<Order> EMPTY_ORDER_PAGE=new PageImpl<Order>(Collections.unmodifiableList(Collections.EMPTY_LIST));

    @Autowired
    public OrderServiceImpl(OrderReposity orderReposity, ValueReposity valueReposity) {
        this.orderReposity=orderReposity;
        this.valueReposity=valueReposity;
    }

    @Override
    public Optional<Order> getById(Integer integer) {
        return orderReposity.findById(integer);
    }

    @Override
    public Order save(Order object) {
        Assert.notNull(object.getPrice(), "无单价");
        Assert.notNull(object.getNum(), "无商品数量");
        Order order=orderReposity.save(object);
        List<Value> values=order.getValueList();
        Iterator<Value> iterator=values.iterator();
        while (iterator.hasNext()) {
            Value val=iterator.next();
            valueReposity.save(val);
        }
        return order;
    }


    @Override
    public OrderReposity getRepository() {
        return orderReposity;
    }

    @Override
    public Page<Order> search(OrderSearchType type, String context, Pageable pageable) {
        Order searchOrder=new Order();
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
        Page<Order> searchResult=getRepository().findAll(Example.of(searchOrder), pageable);
        return searchResult.getSize()==0 ? EMPTY_ORDER_PAGE : searchResult;
    }
}
