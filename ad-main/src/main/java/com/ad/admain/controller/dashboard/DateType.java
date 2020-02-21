package com.ad.admain.controller.dashboard;

import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 01.22.2020
 */
@AllArgsConstructor
public enum DateType implements StringEnum {

    /**
     * 日期类型
     */
    HOUR {
        @Override
        public LocalDateTime plusMin(LocalDateTime time) {
            return time.plusMinutes(10);
        }
    }, DAY {
        @Override
        public LocalDateTime plusMin(LocalDateTime time) {
            return time.plusHours(1);
        }
    }, WEEK {
        @Override
        public LocalDateTime plusMin(LocalDateTime time) {
            return time.plusDays(1);
        }
    }, MONTH {
        @Override
        public LocalDateTime plusMin(LocalDateTime time) {
            return time.plusDays(2);
        }
    }, YEAR {
        @Override
        public LocalDateTime plusMin(LocalDateTime time) {
            return null;
        }
    };


    public abstract LocalDateTime plusMin(LocalDateTime time);

    @Override
    public String getValue() {
        return name().toLowerCase();
    }
}
