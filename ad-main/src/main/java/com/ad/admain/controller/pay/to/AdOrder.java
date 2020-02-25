package com.ad.admain.controller.pay.to;

import com.ad.admain.controller.quartz.entity.JobEntity;
import com.wezhyn.project.annotation.StrategyEnum;
import com.wezhyn.project.database.EnumType;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@EqualsAndHashCode(callSuper=true)
@DynamicUpdate
@NoArgsConstructor
@Entity(name="ad_order")
@Getter
@Setter
@Builder
@AllArgsConstructor
@Accessors(chain=true)
public class AdOrder extends Order {


    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Value> valueList;

    /**
     * 订单状态，订单被更新时，需要更新该属性
     */
    @StrategyEnum(value=EnumType.NUMBER)
    @Type(type="strategyEnum")
    private OrderStatus orderStatus;

    /**
     * 要求广告投放到车上的数量
     */
    private Integer deliverNum;

    /**
     * 订单单价
     */
    private Double price;

    /**
     * 订单数量
     */
    private Integer num;

    private Double latitude;

    private Double longitude;


    /**
     * 广告投放范围
     */

    private Double scope;

    /**
     * 广告投放频率
     */
    private Integer rate;


    @OneToMany(mappedBy="order", targetEntity=JobEntity.class)
    private List<JobEntity> jobEntities;

    /**
     * 投放时间，忽略date
     */
    @Column(nullable=false)
    private LocalDateTime startTime;
    @Column(nullable=false)
    private LocalDateTime endTime;
    /**
     * 开始日期：忽略 time
     */
    @Column(nullable=false)
    private LocalDateTime startDate;
    @Column(nullable=false)
    private LocalDateTime endDate;


    @Override
    public String toString() {
        return "AdOrder{" +
                "valueList=" + valueList +
                ", deliverNum=" + deliverNum +
                ", price=" + price +
                ", num=" + num +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", scope=" + scope +
                ", rate=" + rate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
