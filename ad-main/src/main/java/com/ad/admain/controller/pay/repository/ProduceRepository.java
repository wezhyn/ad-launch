package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.AdProduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : wezhyn
 * @date : 2020/2/25
 */
@Repository
public interface ProduceRepository extends JpaRepository<AdProduce, Integer> {
}
