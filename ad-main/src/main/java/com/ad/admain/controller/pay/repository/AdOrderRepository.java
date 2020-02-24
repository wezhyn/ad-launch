package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.AdOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : wezhyn
 * @date : 2020/2/24
 */
@Repository
public interface AdOrderRepository extends JpaRepository<AdOrder, Integer> {
}
