package com.ad.admain.controller.pay.to;

import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.TradeStatus;
import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.StrategyEnum;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由 {@link OrderService#save(Object)} 创建
 * 订单的支付情况，并保存 Alipay 的信息
 *
 * @author wezhyn
 * @since 12.01.2019
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="ad_bill_info")
@Getter
@Setter
@Table(indexes={
        @Index(name="bill_info_id_total_amount", columnList="gmtPayment,tradeStatus,totalAmount")
})
public class BillInfo implements IBaseTo<Integer> {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;


    @Column(name="order_id")
    private Integer orderId;


    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id", referencedColumnName="id", insertable=false, updatable=false)
    private Order order;

    @Enumerated(value=EnumType.STRING)
    private TradeStatus tradeStatus;

    private Double totalAmount;

    @Type(type="strategyEnum")
    @StrategyEnum(value=com.wezhyn.project.database.EnumType.NUMBER)
    private PayType payType;


    /**
     * 支付宝回调字段
     */
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtPayment;
    private String alipayTradeNo;
    private String outBizNo;
    private String buyerId;
    private String sellerId;


    @Override
    public Integer getId() {
        return orderId;
    }


}
