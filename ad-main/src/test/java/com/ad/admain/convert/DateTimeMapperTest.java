package com.ad.admain.convert;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class DateTimeMapperTest {

    @Test
    public void convertLocalDateTime() {
        final String s=DateTimeMapper.convertLocalDateTime(LocalDateTime.now());
        System.out.println(s);
    }

    @Test
    public void convertLocalDate() {
        final String s="2020-02-28";
        final DateTimeFormatter DEFAULT_SYSTEM_DATE_TIME_FORMAT=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate parse=LocalDate.parse(s, DEFAULT_SYSTEM_DATE_TIME_FORMAT);
        System.out.println(parse);
    }
}