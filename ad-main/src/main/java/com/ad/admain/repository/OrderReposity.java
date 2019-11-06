package com.ad.admain.repository;

import com.ad.admain.to.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderReposity extends JpaRepository<Order, Integer> {
}
