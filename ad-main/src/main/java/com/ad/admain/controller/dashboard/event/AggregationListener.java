package com.ad.admain.controller.dashboard.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 发布事件：{@link AggregationEvent} 用于触发更新(年月日 账单的更新)
 *
 * @author wezhyn
 * @since 01.23.2020
 */
@Component
public class AggregationListener implements ApplicationListener<AggregationEvent> {

    @Override
    @SuppressWarnings("NullableProblems")
    public void onApplicationEvent(AggregationEvent event) {
        System.out.println("public event ======");
        System.out.println(event);

    }
}
