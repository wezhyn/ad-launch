package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.RefundBillInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : wezhyn
 * @date : 2020/2/27
 */
public interface RefundBillInfoRepository extends JpaRepository<RefundBillInfo, Integer> {
}
