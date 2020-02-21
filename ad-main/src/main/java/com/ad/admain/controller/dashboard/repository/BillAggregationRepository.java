package com.ad.admain.controller.dashboard.repository;

import com.ad.admain.controller.dashboard.BillAggregation;
import com.ad.admain.controller.dashboard.DateType;
import com.ad.admain.controller.pay.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 获取某一段时间的账单汇总
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 账单金额
     */
    @Query(nativeQuery=true, value="select sum(total_amount) from ad_bill_info  where gmt_payment between :startTime and :endTime")
    Double accountBillAccount(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取 汇总记录
     *
     * @param type       记录类型
     * @param recordTime 记录时间
     * @return BillAggregation
     */
    BillAggregation getBillAggregationByBillScopeAndRecordTime(DateType type, LocalDateTime recordTime);

    /**
     * 获取某天已经缓存的记录时间
     *
     * @param startTime 某天的00：00
     * @param endTime   某天+1 的00：00
     * @return 时间集
     */
    @Query(nativeQuery=true, value="select record_time from ad_bill_aggregation where bill_scope='hour' and record_time >=:startTime and record_time <=:endTime")
    List<Timestamp> getRecordTimesWithDay(LocalDateTime startTime, LocalDateTime endTime);


    /**
     * 发现某一个时间的记录
     *
     * @param type      类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return billAggregation
     */
    List<BillAggregation> getByBillScopeAndRecordTimeBetween(DateType type, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户数量近似值
     *
     * @return explain
     */
    @Override
    @Query(nativeQuery=true, value="explain select count(id) from ad_generic_user")
    Map<String, Object> getUserNumApproximate();

    /**
     * 获取广告结果近似值
     *
     * @return explain 执行计划
     */
    @Query(nativeQuery=true, value="explain select count(id) from ad_order")
    Map<String, Object> getAdNumApproximate();

    /**
     * 获取 账单数量的近似值
     *
     * @return explain 执行计划返回结果
     */
    @Query(nativeQuery=true, value="explain select count(id) from ad_bill_info")
    Map<String, Object> getBillApproximate();


    public interface BillAccount {
        Integer getMaxId();

        Double getTotalAmount();
    }
}
