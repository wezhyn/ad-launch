package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.ValueService;
import com.ad.admain.controller.pay.repository.ValueReposity;
import com.ad.admain.controller.pay.to.Value;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : lb
 * @date : 2019/12/31
 */
@Service
public class ValueServiceImpl extends AbstractBaseService<Value, Integer> implements ValueService {

    @Autowired
    private ValueReposity valueReposity;


    @Override
    public ValueReposity getRepository() {
        return valueReposity;
    }
}
