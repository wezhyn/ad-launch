package com.ad.admain.service.impl;

import com.ad.admain.repository.OrderReposity;
import com.ad.admain.service.OrderService;
import com.ad.admain.to.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderReposity orderReposity;
    @Autowired
    public OrderServiceImpl(OrderReposity orderReposity) {
        this.orderReposity = orderReposity;
    }

    @Override
    public Optional<Order> getById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> save(Order object) {
       Order order = orderReposity.save(object);
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
