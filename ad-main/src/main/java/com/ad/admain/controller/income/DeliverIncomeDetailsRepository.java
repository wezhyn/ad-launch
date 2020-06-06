package com.ad.admain.controller.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Repository
public interface DeliverIncomeDetailsRepository extends JpaRepository<DriverInComeDetails, Integer> {


    Page<DriverInComeDetails> findByDriverIdAndAmountLessThan(Integer did, double minMount, Pageable pageable);

    Page<DriverInComeDetails> findByDriverId(Integer did, Pageable pageable);

    @Query(nativeQuery = true, value = "select date_format(record_time,'%Y-%m-%d') as date ,sum(amount) as amount" +
            " from driver_in_come_details where driver_id=:did and record_time >=:startTime and record_time <=:endTime" +
            "  group by date_format(record_time,'%Y-%m-%d')")
    List<DriverInComeDayRecord> getDriverRevenueWithDays(Integer did, LocalDateTime startTime, LocalDateTime endTime);


}
