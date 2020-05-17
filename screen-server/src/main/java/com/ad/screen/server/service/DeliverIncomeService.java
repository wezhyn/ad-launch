package com.ad.screen.server.service;

import com.ad.screen.server.dao.DeliverIncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@Service
public class DeliverIncomeService {


    @Autowired
    private DeliverIncomeRepository deliverIncomeRepository;

    @Transactional(rollbackFor = Exception.class)
    public void saveOrIncr(Integer driverId, Double amount) {
        deliverIncomeRepository.saveOrUpdate(driverId, amount);
    }
}
