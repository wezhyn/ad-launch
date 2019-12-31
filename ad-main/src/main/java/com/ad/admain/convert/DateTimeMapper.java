package com.ad.admain.convert;

import com.wezhyn.project.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * @author wezhyn
 * @since 12.23.2019
 */
@Slf4j
@Mapper(componentModel="spring")
public class DateTimeMapper {

    public static final String DEFAULT_SYSTEM_DATE_TIME_PATTERN_NAME="custom.date.time.format";
    public static final String DEFAULT_SYSTEM_DATE_TIME_PATTERN_PROPERTY="yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DEFAULT_SYSTEM_DATE_TIME_FORMAT=DateTimeFormatter.ofPattern(DEFAULT_SYSTEM_DATE_TIME_PATTERN_PROPERTY);
    private static DateTimeFormatter SYSTEM_DATE_TIME_FORMAT;

    static {
        try (InputStream inputStream=new ClassPathResource("config.properties").getInputStream()) {
            Properties configProperty=new Properties();
            configProperty.load(inputStream);
            String dateFormat=configProperty.getProperty(DEFAULT_SYSTEM_DATE_TIME_PATTERN_NAME);
            if (dateFormat.isEmpty()) {
                log.warn("无 {} 配置，使用默认 {} 格式解析 时间", DEFAULT_SYSTEM_DATE_TIME_PATTERN_NAME, DEFAULT_SYSTEM_DATE_TIME_PATTERN_PROPERTY);
            }
            SYSTEM_DATE_TIME_FORMAT=dateFormat.isEmpty() ? DEFAULT_SYSTEM_DATE_TIME_FORMAT : DateTimeFormatter.ofPattern(dateFormat);
        } catch (IOException e) {
            SYSTEM_DATE_TIME_FORMAT=null;
            throw new RuntimeException("无系统配置文件");
        }

    }

    private void check() {
        if (SYSTEM_DATE_TIME_FORMAT==null) {
            throw new RuntimeException("无配置文件：config.properties");
        }
    }

    public LocalDateTime parseLocalDateTime(String time) {
        check();
        return StringUtils.isEmpty(time) ? null : LocalDateTime.parse(time, SYSTEM_DATE_TIME_FORMAT);
    }

    public String convertLocalDateTime(LocalDateTime time) {
        check();
        return time==null ? "" : SYSTEM_DATE_TIME_FORMAT.format(time);
    }


}
