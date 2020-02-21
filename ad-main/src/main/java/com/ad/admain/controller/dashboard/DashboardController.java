package com.ad.admain.controller.dashboard;

import com.ad.admain.controller.dashboard.service.AggregationService;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author wezhyn
 * @since 01.22.2020
 */
@RequestMapping("/api/dashboard")
@RestController
public class DashboardController {

    @Autowired
    private AggregationService aggregationService;

    @GetMapping("/{type}")
    public ResponseResult aggregation(
            @PathVariable DateType type
    ) {
        final Future<AggregationDto> day=aggregationService.getAggregation(type, LocalDateTime.now());
        try {
            return ResponseResult.forSuccessBuilder()
                    .withData("data", day.get()).build();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
            return ResponseResult.forFailureBuilder()
                    .withMessage("获取当天账单失败").build();
        }

    }


}
