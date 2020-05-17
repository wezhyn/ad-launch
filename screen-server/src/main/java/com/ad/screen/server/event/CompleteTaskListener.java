package com.ad.screen.server.event;

import com.ad.launch.order.RemoteRevenueServiceI;
import com.ad.launch.order.RevenueConfig;
import com.ad.screen.server.dao.DiskCompletionRepository;
import com.ad.screen.server.entity.DiskCompletion;
import com.ad.screen.server.service.DeliverIncomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;
import java.time.LocalTime;
import java.util.List;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@Component
@Slf4j
public class CompleteTaskListener implements ApplicationListener<CompleteTaskEvent> {
    private final DiskCompletionRepository diskCompletionRepository;
    private final DeliverIncomeService deliverIncomeService;
    private final RemoteRevenueServiceI remoteRevenueService;
    private volatile WeakReference<RevenueConfig> revenueConfig;

    public CompleteTaskListener(DiskCompletionRepository diskCompletionRepository, DeliverIncomeService deliverIncomeService,
                                RemoteRevenueServiceI remoteRevenueService) {
        this.diskCompletionRepository = diskCompletionRepository;
        this.deliverIncomeService = deliverIncomeService;
        this.remoteRevenueService = remoteRevenueService;
    }


    @Override
    public void onApplicationEvent(CompleteTaskEvent completeTaskEvent) {
        final List<DiskCompletion> completions = diskCompletionRepository.findAllByAdOrderId(completeTaskEvent.getOrderId());
        for (DiskCompletion completion : completions) {
            final Double amount = calculateAmount(completion.getExecutedNum());
            deliverIncomeService.saveOrIncr(completion.getDriverId(), amount);
            log.debug("{} 增加收入 {}", completion.getDriverId(), amount);
        }

    }

    public Double calculateAmount(int exeNum) {
        RevenueConfig config = revenueConfig();
        return config.revenue(LocalTime.now()) * exeNum;
    }

    public RevenueConfig revenueConfig() {
        RevenueConfig config = this.revenueConfig.get();
        if (config == null) {
            synchronized (this) {
                config = this.revenueConfig.get();
                if (config == null) {
                    config = remoteRevenueService.getRevenueConfig();
                    this.revenueConfig = new WeakReference<>(config);
                }
            }
        }
        return config;
    }
}
