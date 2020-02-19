package com.ad.admain.controller.dashboard;

import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author wezhyn
 * @since 01.24.2020
 */
public interface CustomAggregationRepository {


    /**
     * 查找某类型的最近日期的汇聚表信息(在查找时间之前)
     *
     * @param type      账单类别
     * @param queryTime 查询的时间
     * @return aggregation
     */
    Optional<BillAggregation> findLast(@Param(value="type") DateType type, LocalDateTime queryTime);

}
