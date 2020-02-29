package com.ad.admain.controller.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 退款：但每个订单只退款一次
 *
 * @ClassName RefundDto
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/26 15:11
 * @Version 1.0
 */
@Accessors(chain=true)
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RefundOrderDto {


    private int id;

    /**
     * 广告订单编号
     */
    private Integer adOrderId;

    /**
     * 支付宝交易号
     */
    private String tradeNo;
    /**
     * 订单退款币种信息
     */
    private String refundCurrency;
    /**
     * 退款的原因
     */
    private String refundReason;

    private Double refundAmount;

    /**
     * 商户的操作员编号
     */
    private String operatorId;
}
