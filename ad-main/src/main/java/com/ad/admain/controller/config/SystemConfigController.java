package com.ad.admain.controller.config;

import com.ad.launch.order.RevenueConfig;
import com.alibaba.fastjson.JSONObject;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@RestController()
@RequestMapping("/api/system")
public class SystemConfigController {

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @GetMapping("/revenue")
    public ResponseResult revenueGet() {
        return ResponseResult.forSuccessBuilder()
                .withData("revenue", applicationConfigService.getRevenueConfig().toJson())
                .build();
    }

    @PostMapping("/revenue")
    public ResponseResult revenue(@RequestBody JSONObject revenue) {
        try {
            final RevenueConfig config = RevenueConfig.fromJson(revenue.getString("revenue"));
            applicationConfigService.update(config);
        } catch (Exception e) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("数据格式错误")
                    .build();
        }
        return ResponseResult.forSuccessBuilder()
                .withMessage("更新成功")
                .build();
    }
}
