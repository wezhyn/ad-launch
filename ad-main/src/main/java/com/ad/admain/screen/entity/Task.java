package com.ad.admain.screen.entity;

import com.ad.admain.controller.pay.to.AdOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "repeat_count")
    Integer repeatCount;

    @Column(name = "status")
    Boolean status;

    @Column(name = "ad_order_id")
    Integer adOrderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ad_order_id",insertable = false,updatable = false)
    AdOrder order;
}
