package com.ad.admain.controller.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
