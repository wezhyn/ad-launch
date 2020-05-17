package com.ad.screen.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@Getter
public class CompleteTaskEvent extends ApplicationEvent {

    private final Integer orderId;


    public CompleteTaskEvent(Object source, Integer orderId) {
        super(source);
        this.orderId = orderId;
    }
}
