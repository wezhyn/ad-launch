package com.ad.admain.convert;

import org.junit.Test;

import java.time.LocalDateTime;

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
}