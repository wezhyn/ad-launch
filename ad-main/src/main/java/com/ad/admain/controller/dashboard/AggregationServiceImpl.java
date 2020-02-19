package com.ad.admain.controller.dashboard;

import com.ad.admain.controller.dashboard.event.AggregationEvent;
import com.ad.admain.controller.pay.TradeStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

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

    public AggregationServiceImpl(BillAggregationRepository billAggregationRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.billAggregationRepository=billAggregationRepository;
        this.applicationEventPublisher=applicationEventPublisher;
    }


    @Override
    @Transactional(readOnly=true)
    public AggregationDto getHourAggregation() {

        return getAggregation(DateType.HOUR);

    }


    @Override
    @Transactional(readOnly=true)
    public Optional<AggregationDto> getDayAggregation(LocalDate oneDay) {
        return null;
    }


    public AggregationDto getAggregation(DateType type) {
        final AggregationDto aggregationDto=initNum();
        LocalDateTime currentHour=LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), 0));
//        获取该类型的最后一条统计时间
        final Optional<BillAggregation> lastEntry=find(type, currentHour);
//        获取最后一条统计到的id位置，为空则从0开始
        Integer preIndex=lastEntry.filter(billAggregation->billAggregation.getRecordBillId()!=null).map(BillAggregation::getRecordBillId).orElse(0);
//        统计信息：直到最新(当前小时)
        final BillAggregationRepository.BillAccount billAccount=billAggregationRepository.accountBillAccountUtilHour(
                preIndex, TradeStatus.TRADE_SUCCESS, currentHour);
        aggregationDto.setBillAmount(billAccount.getTotalAmount());
        propagateAggregation(type, currentHour);
        return aggregationDto;
    }


    private Optional<BillAggregation> find(DateType type, LocalDateTime oneTime) {
        BillAggregation search=new BillAggregation();
        search.setBillScope(type);
        search.setRecordTime(oneTime);
        return billAggregationRepository.findOne(Example.of(search));
    }


    public void propagateAggregation(DateType type, LocalDateTime oneTime) {
//
//        发布 小时账单事件时，统计到前一个小时之间的账单数据
//        发布 日账单的时候，统一前一天的账单数据
        applicationEventPublisher.publishEvent(new AggregationEvent(this, type, oneTime));
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
