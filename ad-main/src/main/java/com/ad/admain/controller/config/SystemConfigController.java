package com.ad.admain.controller.config;

import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@RestController("/api/system")
public class SystemConfigController {

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @PostMapping("/revenue")
    public ResponseResult revenue() {
        return ResponseResult.forSuccessBuilder()
                .withData("revenue", applicationConfigService.getRevenueConfig())
                .build();
    }
}
