package com.ad.admain.controller.pay.to;

import com.ad.admain.controller.account.entity.GenericUser;
import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.StrategyEnum;
import com.wezhyn.project.database.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name="ad_order")
@Accessors(chain=true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Order implements IBaseTo<Integer> {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Value> valueList;

    private Date startTime;
    private Date endTime;
    private Date startDate;
    private Date endDate;

    /**
     * 订单单价
     */
    private Double price;

    /**
     * 订单数量
     */
    private Integer num;

    private double latitude;

    private double longitude;

    /**
     * 广告投放范围
     */
    private int scope;

    /**
     * 广告投放频率
     */
    private int rate;

    private int uid;


    @ManyToOne()
    @JoinColumn(name="uid", insertable=false, updatable=false)
    private GenericUser orderUser;

    /**
     * 代客服审核
     * 0: 代审核
     * 1:审核通过
     * -1：审核不通过
     * -2：订单代修改
     */
    @ColumnDefault("'0'")
    @Type(type="strategyEnum")
    @StrategyEnum(value=EnumType.NUMBER)
    private OrderVerify verify;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", num=" + num +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", scope=" + scope +
                ", rate=" + rate +
                ", uid=" + uid +
                ", verify=" + verify +
                '}';
    }


}
