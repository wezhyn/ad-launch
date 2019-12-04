package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.ValueService;
import com.ad.admain.controller.pay.repository.ValueReposity;
import com.ad.admain.controller.pay.to.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValueServiceImpl implements ValueService {

    @Autowired
    private ValueReposity valueReposity;


    @Override
    public Optional<Value> getById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Value> save(Value object) {
        Value value = valueReposity.save(object);
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<Value> update(Value newObject) {
        return Optional.empty();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Page<Value> getList(Pageable pageable) {
        return null;
    }
}
