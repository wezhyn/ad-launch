package com.ad.admain.controller.dashboard.event;

import com.ad.admain.controller.dashboard.BillAggregation;
import com.ad.admain.controller.dashboard.DateType;
import com.ad.admain.controller.dashboard.TimeUtils;
import com.ad.admain.controller.dashboard.repository.BillAggregationRepository;
import com.ad.admain.controller.dashboard.service.AccurateStrategy;
import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * 发布事件：{@link AggregationEvent} 用于触发更新(年月日 账单的更新)
 *
 * @author wezhyn
 * @since 01.23.2020
 */
@Component
@Async
public class AggregationListener implements ApplicationListener<AggregationEvent> {

    private static final Function<BillAggregation, Integer> DAYS_HOUR=b->b.getRecordTime().getHour();
    /**
     * 从 0 开始
     */
    private static final Function<BillAggregation, Integer> WEEKS_DAY=b->b.getRecordTime().getDayOfWeek().getValue() - 1;

    private static final Function<BillAggregation, Integer> MONTHS_DAY=b->b.getRecordTime().getDayOfMonth() - 1;

    private final BillAggregationRepository billAggregationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final TaskExecutor taskExecutor;


    public AggregationListener(BillAggregationRepository billAggregationRepository, ApplicationEventPublisher applicationEventPublisher,
                               @Qualifier(value="publish_task_executor") TaskExecutor taskExecutor) {
        this.billAggregationRepository=billAggregationRepository;
        this.applicationEventPublisher=applicationEventPublisher;
        this.taskExecutor=taskExecutor;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void onApplicationEvent(AggregationEvent event) {
        Assert.notNull(event, "监听事件不应该为null");
        LocalDateTime handleTime=event.getHandleTime();
        final TimeUtils.TimeWrap timeBound=TimeUtils.timeBound(event.getDateType(), handleTime);
        switch (event.getDateType()) {
            case HOUR: {
                Assert.notNull(timeBound, "监听事件：" + event.toString() + " 失败");
                Double account=billAggregationRepository.accountBillAccount(timeBound.getStartTime(), timeBound.getEndTime());
                account=account==null ? 0 : account;
                final BillAggregation aggregation=BillAggregation.builder()
                        .accurate(event.isAccurate()).billScope(event.getDateType()).billSum(account)
                        .recordTime(TimeUtils.standardTime(DateType.HOUR, event.getHandleTime())).recordBillId(1).build();
                reflectResult(event, aggregation);
                break;
            }
            case DAY: {
                populate(event, DateType.HOUR, DAYS_HOUR);
                break;
            }
            case WEEK:
                populate(event, DateType.DAY, WEEKS_DAY);
                break;
            case MONTH:
                populate(event, DateType.DAY, MONTHS_DAY);
                break;
            case YEAR:
                break;
            default: {
                throw new RuntimeException("");
            }
        }
    }

    private void populate(AggregationEvent originEvent, DateType prepareType, Function<BillAggregation, Integer> timeExtract) {
        DateType type=originEvent.getDateType();
        LocalDateTime handleTime=originEvent.getHandleTime();
        LocalDateTime standardTime=TimeUtils.standardTime(originEvent.getDateType(), handleTime);
        final TimeUtils.TimeWrap timeBound=TimeUtils.timeBound(type, handleTime);
        final List<BillAggregation> recordedTimes=billAggregationRepository.getByBillScopeAndRecordTimeBetween(prepareType, timeBound.getStartTime(), timeBound.getEndTime());
        recordedTimes.sort(Comparator.comparingInt(timeExtract::apply));
        AtomicDouble sum=new AtomicDouble(0);
        AtomicBoolean isError=new AtomicBoolean(false);
        int trace=0;
        for (BillAggregation b : recordedTimes) {
//                    计算未又缓存的时间
            while (trace < timeExtract.apply(b)) {
//                        同步累加
                final FutureTask<BillAggregation> futureTask=populateEvent(modifyPopulateTime(trace, handleTime, type),
                        prepareType, originEvent);
                isError.compareAndSet(false, addSum(futureTask, sum));
                trace++;
            }
            trace++;
//                   判断当前查询是否是非精确
            final boolean isAccurate=AccurateStrategy.ACCURATE_INSTANCE.isAccurate(b.getRecordTime());
            if (isAccurate!=b.getAccurate()) {
                applicationEventPublisher.publishEvent(new AggregationEvent(originEvent.getSource(), prepareType, isAccurate, b.getRecordTime()));
            }
            sum.addAndGet(b.getBillSum());
        }
        int maxNums=populateEventMaxNums(originEvent.getDateType(), originEvent.getHandleTime());
        while (trace < maxNums) {
            final FutureTask<BillAggregation> futureTask=populateEvent(modifyPopulateTime(trace, handleTime, type),
                    prepareType, originEvent);
            isError.compareAndSet(false, addSum(futureTask, sum));
            trace++;
        }
        boolean isAccurate=!isError.get() && AccurateStrategy.ACCURATE_INSTANCE.isAccurate(standardTime);
        BillAggregation billAggregation=BillAggregation.builder()
                .billScope(originEvent.getDateType()).billSum(sum.get()).recordTime(standardTime)
                .accurate(isAccurate).recordBillId(1).build();
        reflectResult(originEvent, billAggregation);
    }

    private boolean addSum(Future<BillAggregation> task, AtomicDouble sum) {
        try {
            final BillAggregation aggregation=task.get();
            if (aggregation==null) {
                return true;
            }
            sum.addAndGet(aggregation.getBillSum());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param prepareTime 想要查询的时间
     * @param prepareType 想要查询的事件类型
     * @param originEvent 原始事件
     * @return future
     */
    private FutureTask<BillAggregation> populateEvent(LocalDateTime prepareTime, DateType prepareType, AggregationEvent originEvent) {
        final FutureTask<BillAggregation> task=new FutureTask<>(()->{
            boolean isAccurate=AccurateStrategy.ACCURATE_INSTANCE.isAccurate(prepareTime);
            AggregationEvent hourEvent=new AggregationEvent(originEvent.getSource(), prepareType, isAccurate, prepareTime);
            this.onApplicationEvent(hourEvent);
            return hourEvent.get();
        });
        task.run();
        return task;
    }


    /**
     * 修改相对于当前 handleTime 偏移 i 的时间
     *
     * @param i          从0 开始的偏移量
     * @param handleTime handleTime
     * @param type       当前事件类型
     * @return 偏移时间
     */
    private LocalDateTime modifyPopulateTime(int i, LocalDateTime handleTime, DateType type) {
        LocalDate date=handleTime.toLocalDate();
        switch (type) {
            case DAY:
                return LocalDateTime.of(date, LocalTime.of(i, 0));
            case WEEK:
                return LocalDateTime.of(date.with(DayOfWeek.of(i + 1)), LocalTime.MIN);
            case MONTH:
                return LocalDateTime.of(date.withDayOfMonth(i + 1), LocalTime.MIN);
            default: {
                throw new RuntimeException();
            }
        }
    }

    /**
     * 当前事件最大的查询时间
     *
     * @param type       原事件类型
     * @param handleTime 时间
     * @return i
     */
    private int populateEventMaxNums(DateType type, LocalDateTime handleTime) {
        LocalDate now=LocalDate.now();
        switch (type) {
            case DAY: {
                if (handleTime.isAfter(LocalDateTime.of(now, LocalTime.MIN))) {
                    return LocalTime.now().getHour() + 1;
                }
                return 24;
            }
            case WEEK: {
                if (handleTime.isAfter(LocalDateTime.of(now.with(DayOfWeek.MONDAY), LocalTime.MIN))) {
                    return LocalDateTime.now().getDayOfWeek().getValue();
                }
                return 7;
            }
            case MONTH: {
                if (handleTime.isAfter(LocalDateTime.of(now.withDayOfMonth(1), LocalTime.MIN))) {
                    return now.getDayOfMonth();
                }
                return handleTime.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth() + 1;
            }
            default: {
                throw new RuntimeException("不支持的类型");
            }
        }
    }


    private void reflectResult(AggregationEvent event, BillAggregation value) {
        Object output;
        boolean isError=false;
        try {
            output=billAggregationRepository.save(value);
        } catch (Exception e) {
            output=e;
            isError=true;
        }
        final Method setMethod=ReflectionUtils.findMethod(AggregationEvent.class, "set", Object.class);
        Assert.notNull(setMethod, "反射失败");
        setMethod.setAccessible(true);
        try {
            setMethod.invoke(event, output);
        } catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (isError) {
            throw (RuntimeException) output;
        }
    }
}
