package com.ad.admain.service.impl;

import com.ad.admain.repository.OrderReposity;
import com.ad.admain.repository.ValueReposity;
import com.ad.admain.service.OrderService;
import com.ad.admain.to.Order;
import com.ad.admain.to.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderReposity orderReposity;
    private ValueReposity valueReposity;
    @Autowired
    public OrderServiceImpl(OrderReposity orderReposity,ValueReposity valueReposity) {
        this.orderReposity = orderReposity;
        this.valueReposity = valueReposity;
    }

    @Override
    public Optional<Order> getById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> save(Order object) {
       Order order = orderReposity.save(object);
        List<Value> values = order.getValueList();
        Iterator<Value> iterator = values.iterator();
        while (iterator.hasNext()){
            Value val = iterator.next();
            valueReposity.save(val);
        }
        return Optional.ofNullable(order);

    }

    @Override
    public Optional<Order> update(Order newObject) {
        return Optional.empty();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Page<Order> getList(Pageable pageable) {
        return null;
    }
}
