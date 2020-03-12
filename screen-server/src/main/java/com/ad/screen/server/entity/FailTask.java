package com.ad.screen.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName FailTask
 * @Description 熄火导致失败的任务
 * @Author ZLB_KAM
 * @Date 2020/3/9 8:40
 * @Version V1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="fail_task")
public class FailTask {

    @Id
    @Column(name="order_id")
    private Integer orderId;
    @Column(name="num")
    private Integer num;

    @Column(name="uid")
    private Integer uid;


}

