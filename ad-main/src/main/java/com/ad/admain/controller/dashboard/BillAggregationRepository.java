package com.ad.admain.controller.dashboard;

import com.ad.admain.controller.pay.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author wezhyn
 * @since 01.22.2020
 */
@Repository
public interface BillAggregationRepository extends JpaRepository<BillAggregation, Integer>, CustomAggregationRepository, UserAggregationRepository {


    @Query(value="select max(id) as maxId,sum(totalAmount) as totalAmount from ad_bill_info where id > :etfId and tradeStatus=:status " +
            "and gmtPayment > :startTime")
    BillAccount accountBillAccountUtilHour(Integer etfId, TradeStatus status, LocalDateTime startTime);


    @Override
    @Query(nativeQuery=true, value="explain select count(id) from ad_generic_user")
    Map<String, Object> getUserNumApproximate();

    @Query(nativeQuery=true, value="explain select count(id) from ad_order")
    Map<String, Object> getAdNumApproximate();

    @Query(nativeQuery=true, value="explain select count(id) from ad_bill_info")
    Map<String, Object> getBillApproximate();


    public interface BillAccount {
        Integer getMaxId();

        Double getTotalAmount();
    }
}
