package com.ad.screen.server.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @ClassName TaskKey
 * @Description FailTask复合主键
 * @Author ZLB_KAM
 * @Date 2020/3/16 13:43
 * @Version V1.0
 **/

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class TaskKey implements Serializable {


    /**
     * 订单id
     */
    @Column(name="oid")
    private Integer oid;

    /**
     * 发布订单的用户id
     */
    @Column(name="uid")
    private Integer uid;


}
