package com.ad.admain.utils;

import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wezhyn
 * @since 12.26.2019
 */
public class CustomJsonDateDeserializerTest {

    @Test
    public void deserialize() throws ParseException {
        String time="2019-12-26T05:59:12.436Z";
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println(LocalDateTime.parse(time, dateTimeFormatter));
    }
}