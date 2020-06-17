package com.ad.admain.controller.config;

import com.ad.launch.order.RevenueConfig;
import com.alibaba.fastjson.JSONObject;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

        final RevenueConfig config = applicationConfigService.getRevenueConfig();
        List<Double> revenueList = new ArrayList<>();
        for (RevenueConfig.RevenueConfigPair revenueConfigPair : config.getAll()) {
            revenueList.add(revenueConfigPair.getRevenue());
        }
        return ResponseResult.forSuccessBuilder()
                .withData("revenue", revenueList)
                .build();
    }

    @PostMapping("/revenue")
    @SuppressWarnings("unchecked")
    public ResponseResult revenue(@RequestBody JSONObject revenue) {
        try {
            final ArrayList<Double> revenueList = revenue.getObject("revenue", ArrayList.class);
            final RevenueConfig defaultConfig = new RevenueConfig();
            final RevenueConfig.RevenueConfigPair[] pairs = defaultConfig.getAll();
            for (int i = 0; i < pairs.length; i++) {
                if (!pairs[i].getRevenue().equals(revenueList.get(i))) {
                    pairs[i].setRevenue(revenueList.get(i));
                }
            }
            applicationConfigService.update(defaultConfig);
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
