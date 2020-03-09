package com.ad.admain.screen.entity;

import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.controller.pay.to.AdOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    /**
     * 总共执行的次数
     */
    @Column(name = "executed_times")
    Integer executedTimes;
    /**
     * 订单的编号
     */
    @Column(name = "ad_order_id")
    Integer ad_order_id;

    @OneToOne
    @JoinColumn(name = "ad_order_id",insertable = false,updatable = false)
    AdOrder adOrder;

    @Column(name = "uid")
    Integer uid;
    /**
     * 接收订单用户
     */
    @OneToOne
    @JoinColumn(name = "uid",insertable = false,updatable = false)
    GenericUser genericUser;
    /**
     * 是否已经提现
     */
    @Column(name = "is_paid")
    Boolean isPaid;
}

