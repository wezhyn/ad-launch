package com.ad.admain.controller.dashboard.service;

import com.ad.admain.controller.dashboard.AggregationDto;
import com.ad.admain.controller.dashboard.DateType;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

/**
 * 汇总信息的查询大致分成三个步骤：
 * 1. 查询汇总表 ad_bill_aggregation
 * 2. 若无汇总信息，发送一个汇总事件，等待 {@link com.ad.admain.controller.dashboard.event.AggregationListener}
 * 处理
 * 3. 接收者根据返回值适当调整
 *
 * @author wezhyn
 * @since 01.22.2020
 */
public interface AggregationService {


    /**
     * 获取某时间内的账单汇总 ):只做了账单的汇总
     *
     * @param handleTime 要处理的时间
     * @return dto
     */
    Future<AggregationDto> getAggregation(DateType type, LocalDateTime handleTime);

    /**
     * 获取某小时内的账单汇总 ):只做了账单的汇总
     *
     * @param handleTime 要处理的时间
     * @return dto
     */
    Future<AggregationDto> getHourAggregation(LocalDateTime handleTime);

    /**
     * 获取当天内的账单汇总 B:只做了账单的汇总,如果未作统计，则通知统计子系统进行统计
     *
     * @param handleTime 处理时间
     * @return dto：已统计，null：未统计
     */
    Future<AggregationDto> getDayAggregation(LocalDateTime handleTime);

    /**
     * 获取当周的账单金额
     *
     * @param handleTime 某一周
     * @return week
     */
    Future<AggregationDto> getWeekAggregation(LocalDateTime handleTime);

    /**
     * 获取当月账单金额
     *
     * @param handleTime handleTime
     * @return month
     */
    Future<AggregationDto> getMonthAggregation(LocalDateTime handleTime);
}
