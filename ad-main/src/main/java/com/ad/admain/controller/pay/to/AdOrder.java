package com.ad.admain.controller.pay.to;

import com.wezhyn.project.annotation.StrategyEnum;
import com.wezhyn.project.database.EnumType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@EqualsAndHashCode(callSuper=true)
@DynamicUpdate
@DynamicInsert
@Entity(name="ad_order")
@Getter
@Setter
@Accessors(chain=true)
public class AdOrder extends Order implements IProduce {


    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private AdProduce produce;

    /**
     * 订单状态，订单被更新时，需要更新该属性
     */
    @StrategyEnum(value=EnumType.NUMBER)
    @Type(type="strategyEnum")
    @ColumnDefault("'0'")
    private OrderStatus orderStatus;


    /**
     * 创建订单时使用
     *
     * @param uid     用户id
     * @param produce 广告
     */
    public AdOrder(Integer uid, AdProduce produce) {
        super(uid, (produce.getPrice()*produce.getNum()), OrderVerify.WAIT_VERITY);
        Assert.notNull(produce, "广告内容为空，不允许的操作");
        this.produce=produce;
    }

    public AdOrder() {
        super();
    }

    @Override
    public List<String> getProduceContext() {
        return produce.getProduceContext();
    }

    @Override
    public Integer getDeliverNum() {
        return produce.getDeliverNum();
    }

    @Override
    public Double getPrice() {
        return this.produce.getPrice();
    }

    @Override
    public Integer getNum() {
        return produce.getNum();
    }

    @Override
    public Double getLatitude() {
        return produce.getLatitude();
    }

    @Override
    public Double getLongitude() {
        return produce.getLongitude();
    }

    @Override
    public Double getScope() {
        return produce.getScope();
    }

    @Override
    public Integer getRate() {
        return produce.getRate();
    }

    @Override
    public LocalDate getStartDate() {
        return produce.getStartDate();
    }

    @Override
    public LocalDate getEndDate() {
        return produce.getEndDate();
    }

    @Override
    public LocalTime getStartTime() {
        return produce.getStartTime();
    }

    @Override
    public LocalTime getEndTime() {
        return produce.getEndTime();
    }


}
