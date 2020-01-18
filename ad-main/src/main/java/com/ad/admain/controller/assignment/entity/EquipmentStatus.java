package com.ad.admain.controller.assignment.entity;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.wezhyn.project.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @ClassName EquipmentStatus
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/18 15:21
 * @Version 1.0
 */
@Entity
@Table(name = "ad_equipment_status")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@DynamicInsert
@DynamicUpdate
public class EquipmentStatus implements IBaseTo<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "equip_id")
    private Equipment equipment;
    /**
     * 车辆每5分钟可处理的广告数为固定值，该值为目前该车辆可以处理的广告数
     */
    @Column(name = "remain")
    private Integer remain;
}
