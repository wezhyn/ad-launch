package com.ad.admain.controller.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聚合数据
 *
 * @author wezhyn
 * @since 01.22.2020
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AggregationDto {

    private Double billAmount;

    private Integer billCount;

    private Integer adCount;

    private Integer userCount;


}
