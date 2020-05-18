package com.ad.screen.server.entity;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Entity(name = "ad_completion")
@Getter
@Table(
        indexes = {
                @Index(name = "order_driver_id_with_day", columnList = "order_id,deliver_id,time_scope", unique = true)
        }
)
@DynamicInsert
@ToString
public class DiskCompletion implements ICompletion {

    /**
     * 执行的次数
     */
    Integer executedNum;
    /**
     * 订单的编号
     */
    @Column(name = "order_id")
    Integer adOrderId;
    /**
     * 设备所有者id
     */
    @Column(name = "deliver_id")
    Integer driverId;

    /**
     * 对应{@link com.ad.launch.order.RevenueConfig 中的几个时间段}
     * 使用{@link com.ad.launch.order.RevenueConfig#revenueScope} 计算
     */
    @Column(name = "time_scope", nullable = false)
    private Integer timeScope;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "record_time", columnDefinition = "timestamp  null  default current_timestamp")
    private LocalDateTime recordTime;

    public DiskCompletion() {
    }


    @Override
    public Integer getDriverId() {
        return driverId;
    }

}
