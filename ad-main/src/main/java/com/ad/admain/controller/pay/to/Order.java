package com.ad.admain.controller.pay.to;

import com.ad.admain.controller.account.entity.GenericUser;
import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.StrategyEnum;
import com.wezhyn.project.database.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 包含一些Order 的基本属性，如： id,对应的User，账单金额，订单状态
 *
 * @author : wezhyn
 * @date : 2020/2/24
 */
@Accessors(chain=true)
@MappedSuperclass
@Setter
@Getter
public class Order implements IBaseTo<Integer>, ITradeInfo {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="uid", insertable=false, updatable=false)
    private GenericUser orderUser;

    private Integer uid;


    private Double totalAmount;


    private Long tradeOut;

    @Column(name="create_time", columnDefinition="timestamp  null  default current_timestamp")
    private LocalDateTime createTime;


    @Column(name="modify_time", columnDefinition="timestamp  null  default current_timestamp on update current_timestamp")
    private LocalDateTime modifyTime;

    /**
     * 标识该订单是否为用户已经删除，不显示
     */
    @Column(name="order_delete", columnDefinition="bit(1) null default 0")
    private Boolean isDelete;


    /**
     * 代客服审核
     * 0: 待审核
     * 1:审核通过
     * -1：审核不通过
     * -2：订单代修改
     */
    @ColumnDefault("'0'")
    @Type(type="strategyEnum")
    @StrategyEnum(value=EnumType.NUMBER)
    private OrderVerify verify;

    public Order() {
        isDelete=false;
    }

    public Order(Integer uid, Double totalAmount, OrderVerify verify) {
        this.uid=uid;
        this.totalAmount=totalAmount;
        this.verify=verify;
        this.tradeOut=uniqueId();
        isDelete=false;
    }

    protected Long uniqueId() {
        return System.currentTimeMillis() + Thread.currentThread().getId() + ThreadLocalRandom.current().nextInt(10);
    }

    @Override
    public Integer getTradeNo() {
        return id;
    }
}
