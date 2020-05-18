package com.ad.screen.server.mq;

import com.ad.launch.order.CompleteTaskMessage;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
public interface CompleteTaskCallback {

    void send(CompleteTaskMessage message);
}
