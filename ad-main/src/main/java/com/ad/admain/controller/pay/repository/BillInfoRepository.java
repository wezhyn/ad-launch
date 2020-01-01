package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.BillInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public interface BillInfoRepository extends JpaRepository<BillInfo, Integer> {


    /**
     * @param integer
     * @return
     */
    Optional<BillInfo> findByOrderId(Integer integer);
}
