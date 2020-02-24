package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.AdBillInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public interface BillInfoRepository extends JpaRepository<AdBillInfo, Integer> {


    /**
     * @param integer
     * @return
     */
    Optional<AdBillInfo> findByOrderId(Integer integer);
}
