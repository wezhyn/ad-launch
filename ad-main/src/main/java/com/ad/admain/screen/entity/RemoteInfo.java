package com.ad.admain.screen.entity;

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
 * @ClassName RemoteIpEntity
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/23 1:55
 * @Version 1.0
 */
@Entity
@Table(name="screen_remote_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
public class RemoteInfo implements IBaseTo<Integer> {

    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    Integer id;

    @Column(name="ip")
    String ip;

    @Column(name="port")
    Integer port;

    @OneToOne
    @JoinColumn(name="equipment_id", referencedColumnName="id")
    Equipment equipment;


}
