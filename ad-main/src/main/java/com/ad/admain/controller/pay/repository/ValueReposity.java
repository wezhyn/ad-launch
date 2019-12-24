package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueReposity extends JpaRepository<Value, Integer> {
}
