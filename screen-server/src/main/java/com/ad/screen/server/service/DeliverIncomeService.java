package com.ad.screen.server.service;

import java.time.LocalDateTime;

import com.ad.screen.server.dao.DeliverIncomeDetailsRepository;
import com.ad.screen.server.dao.DeliverIncomeRepository;
import com.ad.screen.server.entity.DriverInComeDetails;
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
    @Autowired
    private DeliverIncomeDetailsRepository deliverIncomeDetailsRepository;

    @Transactional(rollbackFor = Exception.class)
    public void saveOrIncr(Integer driverId, Double amount, LocalDateTime time, Integer oid) {
        deliverIncomeRepository.saveOrUpdate(driverId, amount);
        deliverIncomeDetailsRepository.save(new DriverInComeDetails(driverId, amount, oid, time));
    }
}
