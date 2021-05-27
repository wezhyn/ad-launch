package com.ad.screen.server.event;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.List;

import com.ad.launch.order.CompleteTaskMessage;
import com.ad.launch.order.RemoteRevenueServiceI;
import com.ad.launch.order.RevenueConfig;
import com.ad.screen.server.dao.DiskCompletionRepository;
import com.ad.screen.server.entity.DiskCompletion;
import com.ad.screen.server.mq.CompleteTaskCallback;
import com.ad.screen.server.service.DeliverIncomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

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
    private final CompleteTaskCallback completeTaskCallback;

    public CompleteTaskListener(DiskCompletionRepository diskCompletionRepository,
        DeliverIncomeService deliverIncomeService,
        RemoteRevenueServiceI remoteRevenueService, CompleteTaskCallback completeTaskCallback) {
        this.diskCompletionRepository = diskCompletionRepository;
        this.deliverIncomeService = deliverIncomeService;
        this.remoteRevenueService = remoteRevenueService;
        RevenueConfig revenue;
        try {
            revenue = remoteRevenueService.getRevenueConfig();
        } catch (Exception e) {
            revenue = null;
        }
        this.revenueConfig = new WeakReference<>(revenue);
        this.completeTaskCallback = completeTaskCallback;
    }

    @Override
    public void onApplicationEvent(CompleteTaskEvent completeTaskEvent) {
        final List<DiskCompletion> completions = diskCompletionRepository.findAllByAdOrderId(
            completeTaskEvent.getOrderId());
        BigDecimal cost = new BigDecimal(0);
        for (DiskCompletion completion : completions) {
            final Double amount = calculateAmount(completion.getExecutedNum(), completion.getTimeScope());
            cost = cost.add(new BigDecimal(amount));
            deliverIncomeService.saveOrIncr(completion.getDriverId(), amount, completion.getRecordTime());
            log.debug("{} 增加收入 {}", completion.getDriverId(), amount);
        }
        completeTaskCallback.send(new CompleteTaskMessage(completeTaskEvent.getOrderId(), cost.doubleValue()));
    }

    public Double calculateAmount(int exeNum, int timeScope) {
        RevenueConfig config = revenueConfig();
        return BigDecimal.valueOf(config.revenue(timeScope)).multiply(new BigDecimal(exeNum))
            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
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
