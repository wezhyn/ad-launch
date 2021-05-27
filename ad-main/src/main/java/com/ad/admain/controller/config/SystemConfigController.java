package com.ad.admain.controller.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.ad.launch.order.RevenueConfig;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            final JSONArray revenueArray = revenue.getJSONArray("revenue");
            if (revenueArray == null) {
                return ResponseResult.forFailureBuilder().withMessage("无参数信息").build();
            }
            final List<Double> revenueList = revenueArray.stream()
                .map(Object::toString)
                .map(Double::valueOf)
                .collect(Collectors.toList());
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
