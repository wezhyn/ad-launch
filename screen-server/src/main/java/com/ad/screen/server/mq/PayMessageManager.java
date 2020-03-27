package com.ad.screen.server.mq;

import com.ad.launch.order.TaskMessage;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
public interface PayMessageManager {


    /**
     * 检查接收到的信息是否需要过滤
     *
     * @param taskMessage taskMessage
     * @return true: 过滤消息，不执行
     */
    boolean isDuplicate(TaskMessage taskMessage);


    Long getTaskMessageCertificate(TaskMessage taskMessage);
}
