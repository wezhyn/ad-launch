package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.RefundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundOrderRepository extends JpaRepository<RefundOrder,Integer> {
}
