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
import java.time.LocalDateTime;
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


    @ManyToOne()
    @JoinColumn(name="uid", insertable=false, updatable=false)
    private GenericUser orderUser;


    @OneToMany(cascade=CascadeType.ALL)
    private List<Value> valueList;

    @Column(nullable=false)
    private LocalDateTime startTime;
    @Column(nullable=false)
    private LocalDateTime endTime;
    @Column(nullable=false)
    private LocalDateTime startDate;
    @Column(nullable=false)
    private LocalDateTime endDate;


    /**
     * 订单单价
     */
    private Double price;

    /**
     * 订单数量
     */
    private Integer num;

    private Double latitude;

    private Double longitude;

    /**
     * 广告投放范围
     */
    private Integer scope;

    /**
     * 广告投放频率
     */
    private Integer rate;

    private Integer uid;


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
