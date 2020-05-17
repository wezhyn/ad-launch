package com.ad.admain.controller.config;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
public interface ApplicationConfigRepository extends JpaRepository<ApplicationConfig, ApplicationConfigId> {
}
