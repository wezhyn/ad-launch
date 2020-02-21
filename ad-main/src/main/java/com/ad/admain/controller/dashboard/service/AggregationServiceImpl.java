package com.ad.admain.controller.dashboard.service;

import com.ad.admain.controller.dashboard.AggregationDto;
import com.ad.admain.controller.dashboard.BillAggregation;
import com.ad.admain.controller.dashboard.DateType;
import com.ad.admain.controller.dashboard.TimeUtils;
import com.ad.admain.controller.dashboard.event.AggregationEvent;
import com.ad.admain.controller.dashboard.repository.BillAggregationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 具有以下作用：
 * 1. 用于汇总数据的获取
 * 2. 发布事件：触发汇总数据的更新
 *
 * @author wezhyn
 * @since 01.22.2020
 */
@Service
public class AggregationServiceImpl implements AggregationService {


    private final BillAggregationRepository billAggregationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TaskExecutor taskExecutor;


    public AggregationServiceImpl(BillAggregationRepository billAggregationRepository, ApplicationEventPublisher applicationEventPublisher,
                                  @Qualifier(value="publish_task_executor") TaskExecutor taskExecutor) {
        this.billAggregationRepository=billAggregationRepository;
        this.applicationEventPublisher=applicationEventPublisher;
        this.taskExecutor=taskExecutor;
    }

    @Override
    public Future<AggregationDto> getAggregation(DateType type, LocalDateTime handleTime) {
        return doAggregation(handleTime, type);
    }

    @Override
    @Transactional(readOnly=true)
    public Future<AggregationDto> getHourAggregation(LocalDateTime handleTime) {
        return doAggregation(handleTime, DateType.HOUR);
    }

    @Override
    @Transactional(readOnly=true)
    public Future<AggregationDto> getDayAggregation(LocalDateTime handleTime) {
        return doAggregation(handleTime, DateType.DAY);
    }

    @Override
    public Future<AggregationDto> getWeekAggregation(LocalDateTime handleTime) {
        return doAggregation(handleTime, DateType.WEEK);
    }

    @Override
    public Future<AggregationDto> getMonthAggregation(LocalDateTime handleTime) {
        return doAggregation(handleTime, DateType.MONTH);
    }

    private Future<AggregationDto> doAggregation(LocalDateTime handleTime, DateType type) {
        final TimeUtils.TimeWrap timeBound=TimeUtils.timeBound(type, handleTime);
        Assert.notNull(timeBound, "计算时间范围出错");
        LocalDateTime standardTime=TimeUtils.standardTime(type, handleTime);
        final BillAggregation billAggregation=billAggregationRepository.getBillAggregationByBillScopeAndRecordTime(type, standardTime);
        final AggregationDto aggregationDto=initNum();
        FutureTask<AggregationDto> task=new FutureTask<>(()->{
            BillAggregation aggregation=billAggregation;
            if (billAggregation==null) {
                final AggregationEvent event=propagateAggregation(type, handleTime);
                aggregation=event.get();
            } else {
//                当汇总记录非精确时，若距离上次修改时间大于 DateType.plusMin 则重新获取
                if (!aggregation.getAccurate()) {
                    LocalDateTime last=aggregation.getModifyTime()==null ? LocalDateTime.now() : aggregation.getModifyTime();
                    if (type.plusMin(last).isBefore(LocalDateTime.now())) {
                        propagateAggregation(type, handleTime);
                    }
                }
            }
            aggregationDto.setBillAmount(aggregation.getBillSum());
            return aggregationDto;
        });
        taskExecutor.execute(task);
        return task;
    }


    public AggregationEvent propagateAggregation(DateType type, LocalDateTime oneTime) {
//
//        发布 小时账单事件时，统计到前一个小时之间的账单数据
//        发布 日账单的时候，统一前一天的账单数据
        AccurateStrategy accurateStrategy=new AccurateStrategy.CalculateAccurate();
        boolean isAccurate=accurateStrategy.isAccurate(oneTime);
        final AggregationEvent event=new AggregationEvent(this, type, isAccurate, oneTime);
        applicationEventPublisher.publishEvent(event);
        return event;
    }


    private AggregationDto initNum() {
        final BigInteger userNum=(BigInteger) billAggregationRepository.getUserNumApproximate().getOrDefault("rows", 0);
        final BigInteger adNum=(BigInteger) billAggregationRepository.getAdNumApproximate().getOrDefault("rows", 0);
        final BigInteger billNum=(BigInteger) billAggregationRepository.getBillApproximate().getOrDefault("rows", 0);
        return AggregationDto.builder()
                .adCount(adNum.intValue())
                .userCount(userNum.intValue())
                .billCount(billNum.intValue()).build();
    }


}
