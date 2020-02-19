package com.ad.admain.controller.dashboard;

import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.StrategyEnum;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 账单汇总
 *
 * @author wezhyn
 * @since 01.22.2020
 */
@Data
@Entity(name="ad_bill_aggregation")
public class BillAggregation implements IBaseTo<Integer> {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;


    /**
     * 账单类型：月账单，日账单，年账单
     */
    @Column(name="bill_scope")
    @Type(type="strategyEnum")
    @StrategyEnum
    private DateType billScope;


    /**
     * 某段时间内的成交量，与上一次统计之间的差值
     */
    @Column(name="bill_sum")
    private Double billSum;

    /**
     * 此记录记录截至的账单id
     */
    private Integer recordBillId;


    /**
     * 当前时间段汇总是否是精确的
     * 0： 非精确，需要再次重新计算
     * 1： 较精确
     */
    private Boolean accurate;


    @Column(name="record_time")
    @ColumnDefault("current_timestamp")
    private LocalDateTime recordTime;


    @Column(name="modify_time", columnDefinition="timestamp  null  default current_timestamp on update current_timestamp")
    private LocalDateTime modifyTime;
}
