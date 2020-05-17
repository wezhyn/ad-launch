package com.ad.admain.controller.config;

import com.ad.launch.order.RevenueConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Service
public class ApplicationConfigService {

    @Autowired
    private ApplicationConfigRepository applicationConfigRepository;


    @Transactional(rollbackFor = Exception.class)
    public RevenueConfig update(RevenueConfig revenueConfig) throws IOException {
        final ApplicationConfig saved = applicationConfigRepository.save(new ApplicationConfig(ApplicationConfigId.REVENUE, revenueConfig.toJson()));
        return RevenueConfig.fromJson(saved.getConfigs());
    }

    public RevenueConfig getRevenueConfig() {
        return applicationConfigRepository.findById(ApplicationConfigId.REVENUE)
                .map(c -> {
                    try {
                        return RevenueConfig.fromJson(c.getConfigs());
                    } catch (IOException e) {
                        return null;
                    }
                })
                .orElse(new RevenueConfig());
    }
}
