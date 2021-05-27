package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.RefundBillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author : wezhyn
 * @date : 2020/2/27
 */
public interface RefundBillInfoRepository extends JpaRepository<RefundBillInfo, Integer> {

    @Query(nativeQuery = true,
        value = "select sum(refund_fee) from ad_refund_bill_info where buyer_id=:userId and bill_delete = 0")
    double getUserAmount(Integer userId);

}
