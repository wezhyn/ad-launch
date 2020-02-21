package com.ad.admain.controller.dashboard.repository;

import java.util.Map;

/**
 * @author wezhyn
 * @since 01.22.2020
 */
public interface UserAggregationRepository {

    /**
     * 获取用户的大致人数
     *
     * @return count
     */
    Map<String, Object> getUserNumApproximate();
}
