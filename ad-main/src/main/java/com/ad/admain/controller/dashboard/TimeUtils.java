package com.ad.admain.controller.dashboard;

import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author wezhyn
 * @since 02.20.2020
 */
public class TimeUtils {

    private static final LocalTime MAX_DAY_TIME=LocalTime.of(23, 59);

    public static LocalDateTime standardTime(DateType type, LocalDateTime handleTime) {
        switch (type) {
            case HOUR: {
                return handleTime.withMinute(59);
            }
            case DAY: {
                return LocalDateTime.of(handleTime.toLocalDate(), MAX_DAY_TIME);
            }
            case WEEK: {
//                 本周末 ，非 -7
//                2月3号，星期一，返回下周一：2月9号
                return LocalDateTime.of(handleTime.toLocalDate().with(DayOfWeek.SUNDAY), MAX_DAY_TIME);
            }
            case MONTH: {
                return LocalDateTime.of(handleTime.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()), MAX_DAY_TIME);
            }
            default: {
                throw new RuntimeException();
            }
        }
    }

    /**
     * 根据事件计算要汇总的时间表
     *
     * @param type            type
     * @param eventHandleTime 处理的时间
     * @return 开始时间与结束时间
     */
    public static TimeWrap timeBound(DateType type, LocalDateTime eventHandleTime) {
        LocalDate dayTime=eventHandleTime.toLocalDate();
        switch (type) {
            case HOUR: {
                final int eventHour=eventHandleTime.getHour();
                LocalTime endTime=LocalTime.of((eventHour + 1)%24, 0);
                LocalDate endDate=eventHour==23 ? eventHandleTime.toLocalDate().plusDays(1) : eventHandleTime.toLocalDate();
                return new TimeWrap(eventHandleTime.withMinute(0),
                        LocalDateTime.of(endDate, endTime));
            }
            case DAY: {
                return new TimeWrap(
                        LocalDateTime.of(dayTime, LocalTime.MIN),
                        LocalDateTime.of(dayTime.plusDays(1), LocalTime.MIN)
                );
            }
            case WEEK: {
                return new TimeWrap(
                        LocalDateTime.of(dayTime.with(DayOfWeek.MONDAY), LocalTime.MIN),
                        LocalDateTime.of(dayTime.with(DayOfWeek.SUNDAY), LocalTime.MIN).plusDays(1)
                );
            }
            case MONTH: {
                return new TimeWrap(
                        LocalDateTime.of(dayTime.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN),
                        LocalDateTime.of(dayTime.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MIN).plusDays(1)
                );
            }
            default: {
                throw new RuntimeException("不支持的类型");
            }
        }
    }

    @Value
    public static class TimeWrap {
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
    }

}
