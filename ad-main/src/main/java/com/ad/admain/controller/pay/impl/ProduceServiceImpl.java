package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.ProduceService;
import com.ad.admain.controller.pay.repository.ProduceRepository;
import com.ad.admain.controller.pay.to.AdProduce;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : lb
 * @date : 2019/12/31
 */
@Service
public class ProduceServiceImpl extends AbstractBaseService<AdProduce, Integer> implements ProduceService {

    @Autowired
    private ProduceRepository valueReposity;


    @Override
    public ProduceRepository getRepository() {
        return valueReposity;
    }
}
