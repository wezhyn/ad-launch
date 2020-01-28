package com.ad.admain.controller.quartz.entity;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.to.Order;
import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName JobEntity
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/26 11:24
 * @Version 1.0
 */
@Entity
@Table(name = "JOB_ENTITY")
@Data
@Accessors(chain = true)
public class JobEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;          //job名称
    @Column(name = "job_group")
    private String jobGroup;      //job组名
    @Column(name = "cron")
    private String cron;          //执行的cron
    @Column(name = "description")
    private String description;   //job描述信息
    @Column(name = "status")
    private String status;        //job的执行状态,这里我设置为OPEN/CLOSE且只有该值为OPEN才会执行该Job
    @Column(name = "is_finished")
    private Boolean isFinished = false;     //job的参数
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;      //与订单进行关联
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equip_id",referencedColumnName = "id")
    private Equipment equip;
    @Column(name = "amount")
    private Integer amount;
//    @Column(name = "content")
//    private String content;

}
