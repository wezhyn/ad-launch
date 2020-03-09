package com.ad.admain.screen.entity;

import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.controller.pay.to.AdOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
@Table(name = "fail_task")
public class FailTask {

    @Id
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "num")
    private Integer num;

    @Column(name = "uid")
    private Integer uid;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private AdOrder adOrder;

}

