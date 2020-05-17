package com.ad.admain.remote;

import com.ad.admain.controller.config.ApplicationConfigService;
import com.ad.launch.order.RemoteRevenueServiceI;
import com.ad.launch.order.RevenueConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Component(value = "remoteRevenueService")
public class RemoteRevenueService implements RemoteRevenueServiceI {

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @Override
    public RevenueConfig getRevenueConfig() {
        return applicationConfigService.getRevenueConfig();
    }
}
