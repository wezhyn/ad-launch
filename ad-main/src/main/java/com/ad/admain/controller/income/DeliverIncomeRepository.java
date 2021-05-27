package com.ad.admain.controller.income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
public interface DeliverIncomeRepository extends JpaRepository<DriverInCome, Integer> {


    @Modifying
    @Query(nativeQuery = true, value = "insert into driver_in_come(driver_id,amount)" +
            "values(:driverId,:mount) on duplicate key update amount=amount+:mount")
    int saveOrUpdate(int driverId, double mount);

    @Modifying
    @Query(nativeQuery = true, value = "update driver_in_come set amount=amount-:amount where driver_id=:userId")
    int decrUserAmount(Integer userId, Double amount);

    @Query(nativeQuery = true, value = "select sum(amount) from fkz.driver_in_come_details where driver_id=:userId")
    double getUserAmount(Integer userId);

    @Query(nativeQuery = true, value = "select amount from driver_in_come where driver_id=:userId for update ")
    double getUserAmountSync(Integer userId);

}
