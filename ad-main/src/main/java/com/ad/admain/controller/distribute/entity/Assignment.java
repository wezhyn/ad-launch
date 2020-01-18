package com.ad.admain.controller.distribute.entity;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.to.Order;
import com.wezhyn.project.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @ClassName CarTask
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/18 10:45
 * @Version 1.0
 */
@Entity
@Table(name = "ad_assignment")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@DynamicUpdate
@DynamicInsert
public class Assignment implements IBaseTo<Integer> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分配到车上的广告条数
     */
    @Column(name = "num")
    private Long num;
    /**
     * 广告的内容
     */
    @Column(name = "start_time")
    private String content;

    @Column(name = "trigger_time")
    private LocalDateTime triggerTime;

    @ManyToOne
    @JoinColumn(name = "equip_id",referencedColumnName = "id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;
}
