package com.ad.screen.server.config;

import lombok.Getter;
import org.apache.rocketmq.remoting.common.RemotingUtil;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
public class GlobalIdentify {
    public static final GlobalIdentify IDENTIFY=new GlobalIdentify();
    @Getter
    private final String id;

    private GlobalIdentify() {
        id=RemotingUtil.getLocalAddress();
    }
}
