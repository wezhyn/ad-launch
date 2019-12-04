package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderReposity extends JpaRepository<Order, Integer> {
}
