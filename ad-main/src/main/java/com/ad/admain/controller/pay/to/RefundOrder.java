package com.ad.admain.controller.pay.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 非 Order
 *
 * @author wezhyn
 * @since 02.24.2020
 */
@NoArgsConstructor
@Getter
@Setter
public class RefundOrder extends Order {
    /**
     * 支付宝交易号
     * 通过 AdBillInfo获取
     */
//    @Column(name="trade_no")
    private String outTradeNo;

    /**
     * 广告订单编号
     */
    private Integer adOrderId;

/*
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ad_order_id", insertable=false, updatable=false)
    private AdOrder adOrder;
*/
    /**
     * 订单退款币种信息
     */
//    @Column(name="refund_currency")
    private String refundCurrency;
    /**
     * 退款的原因
     * 前台获取
     */
//    @Column(name="refund_reason")
    private String refundReason;
    /**
     * 商户的操作员编号
     */
//    @Column(name="operator_id")
    private String operatorId;

    public RefundOrder(Integer uid, Double totalAmount, OrderVerify verify) {
        super(uid, totalAmount, verify);
    }

    public static RefundOrderBuilder createRefundOrder(Integer uid, Double refundAmount, String refundReason) {
        return new RefundOrderBuilder().uid(uid).totalAmount(refundAmount).refundReason(refundReason)
                .verify(OrderVerify.PASSING_VERIFY)
                ;
    }


    public static final class RefundOrderBuilder {
        private Integer adOrderId;
        private String outTradeNo;
        private Integer id;
        private String refundCurrency;
        private Integer uid;
        private Double totalAmount;
        private String refundReason;
        private Long tradeOut;
        private String operatorId;
        private OrderVerify verify;

        private RefundOrderBuilder() {
        }

        public static RefundOrderBuilder aRefundOrder() {
            return new RefundOrderBuilder();
        }

        public RefundOrderBuilder adOrderId(Integer adOrderId) {
            this.adOrderId=adOrderId;
            return this;
        }

        public RefundOrderBuilder outTradeNo(String outTradeNo) {
            this.outTradeNo=outTradeNo;
            return this;
        }

        public RefundOrderBuilder id(Integer id) {
            this.id=id;
            return this;
        }

        public RefundOrderBuilder refundCurrency(String refundCurrency) {
            this.refundCurrency=refundCurrency;
            return this;
        }


        public RefundOrderBuilder uid(Integer uid) {
            this.uid=uid;
            return this;
        }

        public RefundOrderBuilder totalAmount(Double totalAmount) {
            this.totalAmount=totalAmount;
            return this;
        }

        public RefundOrderBuilder refundReason(String refundReason) {
            this.refundReason=refundReason;
            return this;
        }

        public RefundOrderBuilder tradeOut(Long tradeOut) {
            this.tradeOut=tradeOut;
            return this;
        }

        public RefundOrderBuilder operatorId(String operatorId) {
            this.operatorId=operatorId;
            return this;
        }

        public RefundOrderBuilder verify(OrderVerify verify) {
            this.verify=verify;
            return this;
        }

        public RefundOrder build() {
            RefundOrder refundOrder=new RefundOrder(uid, totalAmount, verify);
            refundOrder.setAdOrderId(adOrderId);
            refundOrder.setOutTradeNo(outTradeNo);
            refundOrder.setRefundCurrency(refundCurrency);
            refundOrder.setRefundReason(refundReason);
            refundOrder.setOperatorId(operatorId);
            refundOrder.setId(id);
            refundOrder.setTradeOut(tradeOut);
            return refundOrder;
        }
    }
}
