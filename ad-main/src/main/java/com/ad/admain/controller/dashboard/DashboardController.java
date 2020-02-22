package com.ad.admain.controller.dashboard;

import com.ad.admain.controller.dashboard.service.AggregationService;
import com.wezhyn.project.controller.ResponseResult;
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

    private final AggregationService aggregationService;

    public DashboardController(AggregationService aggregationService) {
        this.aggregationService=aggregationService;
    }

    @GetMapping("/{type}/{date}")
    public ResponseResult aggregation(
            @PathVariable DateType type,
            @PathVariable LocalDateTime date) {
        LocalDateTime searchTime=date==null ? LocalDateTime.now() : date;
        final Future<AggregationDto> day=aggregationService.getAggregation(type, searchTime);
        try {
            return ResponseResult.forSuccessBuilder()
                    .withData("data", day.get()).build();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
            return ResponseResult.forFailureBuilder()
                    .withMessage("获取当天账单失败").build();
        }
    }

    @RequestMapping("/{type}")
    public ResponseResult aggregation(
            @PathVariable DateType type
    ) {
        return this.aggregation(type, LocalDateTime.now());
    }


}
