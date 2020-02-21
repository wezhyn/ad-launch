package com.ad.admain.controller.dashboard;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

/**
 * @author wezhyn
 * @since 02.20.2020
 */
public class TimeUtilsTest {

    @Test
    public void standardTime() {
        final LocalDateTime now=LocalDateTime.of(2020, 2, 3, 12, 12);
        final LocalDateTime hourTime=TimeUtils.standardTime(DateType.HOUR, now);
        final LocalDateTime hourExpect=LocalDateTime.of(2020, 2, 3, 12, 59);
        assertEquals(hourExpect, hourTime);
        final LocalDateTime dayTime=TimeUtils.standardTime(DateType.DAY, now);
        final LocalDateTime dayExpect=LocalDateTime.of(LocalDate.of(2020, 2, 3), LocalTime.MAX);
        assertEquals(dayExpect, dayTime);
        final LocalDateTime weekTime=TimeUtils.standardTime(DateType.WEEK, now);
        final LocalDateTime weekExpect=LocalDateTime.of(LocalDate.of(2020, 2, 9), LocalTime.MAX);
        assertEquals(weekExpect, weekTime);
        final LocalDateTime monthTime=TimeUtils.standardTime(DateType.MONTH, now);
        final LocalDateTime monthExpect=LocalDateTime.of(LocalDate.of(2020, 2, 29), LocalTime.MAX);
        assertEquals(monthExpect, monthTime);
    }


    @Test
    public void timeBoundWeek() {
        final LocalDateTime now=LocalDateTime.of(2020, 2, 6, 12, 59);
        final TimeUtils.TimeWrap timeBound=TimeUtils.timeBound(DateType.WEEK, now);
        assertEquals(3, timeBound.getStartTime().getDayOfMonth());
        assertEquals(10, timeBound.getEndTime().getDayOfMonth());
    }
}