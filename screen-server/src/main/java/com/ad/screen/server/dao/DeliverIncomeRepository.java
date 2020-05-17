package com.ad.screen.server.dao;

import com.ad.screen.server.entity.DriverInCome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
public interface DeliverIncomeRepository extends JpaRepository<DriverInCome, Integer> {


    @Query(nativeQuery = true, value = "insert into driver_in_come(driver_id,amount)" +
            "values(:driverId,:mount) on duplicate key update amount=amount+:mount")
    DriverInCome saveOrUpdate(int driverId, double mount);

}
