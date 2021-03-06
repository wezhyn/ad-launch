package com.ad.admain.controller.pay.to;

import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.StrategyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@MappedSuperclass
@Data
@AllArgsConstructor
public class BillInfo implements IBaseTo<Integer> {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /**
     * 对应不同 BillInfo ，其对应的Order类型不同
     */
    @Column(name="order_id")
    private Integer orderId;


    private Double totalAmount;

    @Type(type="strategyEnum")
    @StrategyEnum(value = com.wezhyn.project.database.EnumType.NUMBER)
    @Column(nullable = false, name = "pay_type", columnDefinition = "smallint default '0' comment '0：Alipay,1:Wechat' ")
    private PayType payType;

    @Column(name="bill_delete", columnDefinition="bit(1) default 0")
    Boolean delete;

    /**
     * 常用回调字段
     */
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtPayment;
    private String alipayTradeNo;
    private String outBizNo;
    private String buyerId;
    private String sellerId;

    public BillInfo() {
        delete=false;
    }
}
