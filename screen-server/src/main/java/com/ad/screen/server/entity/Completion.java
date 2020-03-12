package com.ad.screen.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @ClassName Completion
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:37
 * @Version V1.0
 **/
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Completion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;

    /**
     * 总共执行的次数
     */
    @Column(name="executed_times")
    Integer executedTimes;
    /**
     * 订单的编号
     */
    @Column(name="ad_order_id")
    Integer adOrderId;


    @Column(name="uid")
    Integer uid;

    /**
     * 是否已经提现
     */
    @Column(name="is_paid")
    Boolean isPaid;
}

