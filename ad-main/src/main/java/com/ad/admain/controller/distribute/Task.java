package com.ad.admain.controller.distribute;

import com.ad.admain.controller.pay.to.Order;
import com.wezhyn.project.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @ClassName Task
 * @Description 任务实体类
 * @Param
 * @Author ZLB
 * @Date 2020/1/16 21:50
 * @Version 1.0
 */
@Entity(name = "ad_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
public class Task implements IBaseTo<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "content")
    private String content;

    @Column(name = "num")
    private Long num;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

}
