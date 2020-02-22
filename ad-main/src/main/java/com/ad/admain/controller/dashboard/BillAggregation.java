package com.ad.admain.controller.dashboard;

import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.StrategyEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicInsert
@DynamicUpdate
@Table(
        indexes=@Index(name="bill_scope_record_time", unique=true, columnList="bill_scope,record_time")
)
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
    @Column(name="bill_sum", columnDefinition="  double(15,3)  null default 0 ")
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


    @Column(name="record_time", columnDefinition="timestamp  null  default current_timestamp")
    private LocalDateTime recordTime;


    @Column(name="modify_time", columnDefinition="timestamp  null  default current_timestamp on update current_timestamp")
    private LocalDateTime modifyTime;

    public static BillAggregationBuilder builder() {
        return new BillAggregationBuilder();
    }


    public static final class BillAggregationBuilder {
        private Integer id;
        private DateType billScope;
        private Double billSum;
        private Integer recordBillId;
        private Boolean accurate;
        private LocalDateTime recordTime;
        private LocalDateTime modifyTime;

        private BillAggregationBuilder() {
        }

        public static BillAggregationBuilder aBillAggregation() {
            return new BillAggregationBuilder();
        }

        public BillAggregationBuilder id(Integer id) {
            this.id=id;
            return this;
        }

        public BillAggregationBuilder billScope(DateType billScope) {
            this.billScope=billScope;
            return this;
        }

        public BillAggregationBuilder billSum(Double billSum) {
            this.billSum=billSum;
            return this;
        }

        public BillAggregationBuilder recordBillId(Integer recordBillId) {
            this.recordBillId=recordBillId;
            return this;
        }

        public BillAggregationBuilder accurate(Boolean accurate) {
            this.accurate=accurate;
            return this;
        }

        public BillAggregationBuilder recordTime(LocalDateTime recordTime) {
            this.recordTime=recordTime;
            return this;
        }

        public BillAggregationBuilder modifyTime(LocalDateTime modifyTime) {
            this.modifyTime=modifyTime;
            return this;
        }

        public BillAggregation build() {
            BillAggregation billAggregation=new BillAggregation();
            billAggregation.setId(id);
            billAggregation.setBillScope(billScope);
            billAggregation.setBillSum(billSum);
            billAggregation.setRecordBillId(recordBillId);
            billAggregation.setAccurate(accurate);
            billAggregation.setRecordTime(recordTime);
            billAggregation.setModifyTime(modifyTime);
            return billAggregation;
        }
    }
}
