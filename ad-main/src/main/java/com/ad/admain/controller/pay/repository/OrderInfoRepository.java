package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.BillInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public interface OrderInfoRepository extends JpaRepository<BillInfo, Integer> {
}
