package com.ad.screen.server.dao;

import com.ad.screen.server.entity.DiskCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
public interface DiskCompletionRepository extends JpaRepository<DiskCompletion, Integer> {


    /**
     * 插入或更新数据
     *
     * @param driverId    车主id
     * @param orderId     订单id
     * @param executedNum executedNum
     * @return i
     */
    @Modifying
    @Query(value = "insert into ad_completion(order_id, deliver_id, executed_num) values (:orderId,:driverId,:executedNum)" +
            "on duplicate key update executed_num=executed_num+:executedNum", nativeQuery = true)
    int createOrIncrComplete(int driverId, int orderId, int executedNum);

    List<DiskCompletion> findAllByAdOrderId(Integer orderId);


    DiskCompletion findByAdOrderIdAndDriverId(int orderId, int driverId);

}
