package com.ad.admain.controller.dashboard.event;

import com.ad.admain.controller.dashboard.BillAggregation;
import com.ad.admain.controller.dashboard.DateType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

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

    private Object output;

    private CountDownLatch watch;


    public AggregationEvent(Object source, DateType dateType, boolean accurate, LocalDateTime handleTime) {
        super(source);
        this.dateType=dateType;
        this.accurate=accurate;
        this.handleTime=handleTime;
        output=null;
        watch=new CountDownLatch(1);
    }

    private void set(Object o) {
        Assert.notNull(o, "非法调用");
        watch.countDown();
        this.output=o;
    }

    public BillAggregation get() {
        try {
            watch.await();
        } catch (InterruptedException ignore) {
        }
        if (output instanceof Exception) {
            throw new RuntimeException((Exception) output);
        }
        return (BillAggregation) output;
    }
}
