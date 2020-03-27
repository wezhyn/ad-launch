package com.ad.screen.server.entity;

import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Entity(name="ad_completion")
@Getter
@Table(
        indexes={
                @Index(name="order_driver_id", columnList="order_id,deliver_id")
        }
)
@DynamicInsert
public class DiskCompletion implements ICompletion {

    /**
     * 执行的次数
     */
    Integer executedNum;
    /**
     * 订单的编号
     */
    @Column(name="order_id")
    Integer adOrderId;
    /**
     * 设备所有者id
     */
    @Column(name="deliver_id")
    Integer driverId;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="record_time", columnDefinition="timestamp  null  default current_timestamp")
    private LocalDateTime recordTime;

    public DiskCompletion() {
    }

    public DiskCompletion(Integer executedNum, Integer adOrderId, Integer driverId) {
        this.executedNum=executedNum;
        this.adOrderId=adOrderId;
        this.driverId=driverId;
    }

    @Override
    public Integer getDriverId() {
        return driverId;
    }

}
