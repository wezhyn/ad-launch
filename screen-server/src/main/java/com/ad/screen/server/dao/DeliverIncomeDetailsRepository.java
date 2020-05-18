package com.ad.screen.server.dao;

import com.ad.screen.server.entity.DriverInComeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Repository
public interface DeliverIncomeDetailsRepository extends JpaRepository<DriverInComeDetails, Integer> {
}
