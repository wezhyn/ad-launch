package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.repository.OrderReposity;
import com.ad.admain.controller.pay.repository.ValueReposity;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.Value;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl extends AbstractBaseService<Order, Integer> implements OrderService {

    private OrderReposity orderReposity;
    private ValueReposity valueReposity;

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
    public Optional<Order> save(Order object) {
        Assert.notNull(object.getPrice(), "无单价");
        Assert.notNull(object.getNum(), "无商品数量");
        Order order=orderReposity.save(object);
        List<Value> values=order.getValueList();
        Iterator<Value> iterator=values.iterator();
        while (iterator.hasNext()) {
            Value val=iterator.next();
            valueReposity.save(val);
        }
        return Optional.of(order);
    }

    @Override
    public Page<Order> getList(Pageable pageable) {
        return null;
    }

    @Override
    public OrderReposity getRepository() {
        return orderReposity;
    }
}
