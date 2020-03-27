package com.ad.screen.server.dao;

import com.ad.screen.server.entity.DiskCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
public interface DiskCompletionRepository extends JpaRepository<DiskCompletion, Integer> {
}
