package com.ad.admain.repository;

import com.ad.admain.to.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueReposity extends JpaRepository<Value,Integer> {
}
