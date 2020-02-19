package com.ad.admain.controller.dashboard;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author wezhyn
 * @since 01.22.2020
 */
public interface AggregationService {


    /**
     * 获取当前小时内的账单汇总 B:只做了账单的汇总
     *
     * @return dto
     */
    AggregationDto getHourAggregation();

    /**
     * 获取当天内的账单汇总 B:只做了账单的汇总,如果未作统计，则通知统计子系统进行统计
     *
     * @return dto：已统计，null：未统计
     */
    Optional<AggregationDto> getDayAggregation(LocalDate oneDay);
}
