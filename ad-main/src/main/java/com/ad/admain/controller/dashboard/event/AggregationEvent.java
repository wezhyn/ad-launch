package com.ad.admain.controller.dashboard.event;

import com.ad.admain.controller.dashboard.DateType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * 用于触发汇聚表的更新
 *
 * @author wezhyn
 * @since 01.23.2020
 */
@Getter
public class AggregationEvent extends ApplicationEvent {

    /**
     * 聚合数据的类型
     */
    private DateType dateType;

    /**
     * 用来暗示当前的事件是否是精确计算
     */
    private boolean accurate;


    private LocalDateTime handleTime;


    public AggregationEvent(Object source, DateType dateType, LocalDateTime handleTime) {
        super(source);
        this.dateType=dateType;
        this.handleTime=handleTime;
    }
}
