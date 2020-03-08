package com.ad.admain.screen.entity;

import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.to.AdOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "repeat_num")
    Integer repeatNum;

    @Column(name = "status")
    Boolean status;

    @Column(name = "ad_order_id")
    Integer adOrderId;

    @Column(name = "vertical_view")
    Boolean verticalView;

    @Column(name = "entry_id")
    Integer entryId;

    @Column(name = "view")
    String view;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ad_order_id",insertable = false,updatable = false)
    AdOrder order;

    @Column(name = "uid")
    Integer uid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid",insertable = false,updatable = false)
    GenericUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "equip_id")
    Equipment equipment;
}
