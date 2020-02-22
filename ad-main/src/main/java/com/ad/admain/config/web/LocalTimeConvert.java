package com.ad.admain.config.web;

import com.ad.admain.convert.DateTimeMapper;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class LocalTimeConvert implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        if (source.length()==0) {
            throw new RuntimeException("时间参数错误");
        }
        return DateTimeMapper.parseLocalDateTime(source);
    }
}
