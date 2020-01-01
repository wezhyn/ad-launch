package com.ad.admain.controller.pay.dto;

import com.ad.admain.controller.pay.OrderService;
import com.wezhyn.project.IBaseTo;
import lombok.*;

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
@Getter
@Setter
public class BillInfoDto implements IBaseTo<Integer> {

    private Integer id;

    private Integer orderId;

    private OrderDto order;

    private String tradeStatus;

    private Double totalAmount;

    private String payType;

    /**
     * 支付宝回调字段
     */
    private String gmtCreate;
    private String gmtPayment;
    private String alipayTradeNo;
    private String outBizNo;
    private String buyerId;
    private String sellerId;


    @Override
    public Integer getId() {
        return orderId;
    }


}
