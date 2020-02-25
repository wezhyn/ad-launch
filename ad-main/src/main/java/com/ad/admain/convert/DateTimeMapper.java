package com.ad.admain.convert;

import com.wezhyn.project.utils.StringUtils;
import com.wezhyn.project.utils.Strings;
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
    public static final String DEFAULT_SYSTEM_DATE_PATTERN_PROPERTY="yyyy-MM-dd";
    public static final String DEFAULT_SYSTEM_TIME_PATTERN_PROPERTY="HH:mm:ss";
    public static final DateTimeFormatter DEFAULT_SYSTEM_DATE_TIME_FORMAT=DateTimeFormatter.ofPattern(DEFAULT_SYSTEM_DATE_TIME_PATTERN_PROPERTY);
    public static final DateTimeFormatter SYSTEM_TIME_FORMAT;
    public static final DateTimeFormatter SYSTEM_DATE_FORMAT;
    private static DateTimeFormatter SYSTEM_DATE_TIME_FORMAT;

    static {
        try (InputStream inputStream=new ClassPathResource("config.properties").getInputStream()) {
            Properties configProperty=new Properties();
            configProperty.load(inputStream);
            String dateTimeFormatter=configProperty.getProperty(DEFAULT_SYSTEM_DATE_TIME_PATTERN_NAME);
            String timeFormat=configProperty.getProperty("custom.time.format");
            String dateFormatter=configProperty.getProperty("custom.date.format");
            if (Strings.isEmpty(dateFormatter)) {
                log.warn("无 {} 配置，使用默认 {} 格式解析 时间", DEFAULT_SYSTEM_DATE_TIME_PATTERN_NAME, DEFAULT_SYSTEM_DATE_TIME_PATTERN_PROPERTY);
            }
            if (Strings.isEmpty(timeFormat)) {
                log.warn("无 {} 配置格式解析 时间", "custom.time.format");
            }
            SYSTEM_DATE_FORMAT=DateTimeFormatter.ofPattern(dateFormatter);
            SYSTEM_TIME_FORMAT=DateTimeFormatter.ofPattern(timeFormat);
            SYSTEM_DATE_TIME_FORMAT=dateTimeFormatter.isEmpty() ? DEFAULT_SYSTEM_DATE_TIME_FORMAT : DateTimeFormatter.ofPattern(dateTimeFormatter);
        } catch (IOException e) {
            SYSTEM_DATE_TIME_FORMAT=null;
            throw new RuntimeException("无系统配置文件");
        }

    }

    private static void check() {
        if (SYSTEM_DATE_TIME_FORMAT==null) {
            throw new RuntimeException("无配置文件：config.properties");
        }
    }

    public static LocalDateTime parseLocalDateTime(String time) {
        check();
        return StringUtils.isEmpty(time) ? null : LocalDateTime.parse(time, SYSTEM_DATE_TIME_FORMAT);
    }

    public static String convertLocalDateTime(LocalDateTime time) {
        check();
        return time==null ? "" : SYSTEM_DATE_TIME_FORMAT.format(time);
    }


}
