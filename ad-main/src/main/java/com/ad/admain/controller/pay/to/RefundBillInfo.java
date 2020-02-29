package com.ad.admain.controller.pay.to;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.27.2020
 */
@Getter
@Entity(name="ad_refund_bill_info")
public class RefundBillInfo extends BillInfo {


    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id", referencedColumnName="id", insertable=false, updatable=false)
    private AdOrder adOrder;

    /**
     * 退款的原因
     * 前台获取
     */
    @Column(name="refund_reason")
    private String refundReason;

    /**
     * 订单退款币种信息
     */
    @Column(name="refund_currency")
    private String refundCurrency;

    /**
     * 退款人员
     */
    private String operatorId;

    /**
     * 退款总额
     */
    private Double refundFee;

    public static RefundBillInfoBuilder fromOrder(RefundOrder order) {
        return new RefundBillInfoBuilder()
                .orderId(order.getId())
                .operatorId(order.getOperatorId())
                .refundCurrency(order.getRefundCurrency())
                .refundReason(order.getRefundReason())
                .outBizNo(order.getOutTradeNo())
                .payType(PayType.ALI_PAY)
                .totalAmount(order.getTotalAmount());

    }


    public static final class RefundBillInfoBuilder {
        private String refundReason;
        private Integer id;
        private String refundCurrency;
        private String operatorId;
        private Integer orderId;
        private Double totalAmount;
        private Double refundFee;
        private PayType payType;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtPayment;
        private String alipayTradeNo;
        private String outBizNo;
        private String buyerId;
        private String sellerId;

        private RefundBillInfoBuilder() {
        }

        public static RefundBillInfoBuilder aRefundBillInfo() {
            return new RefundBillInfoBuilder();
        }

        public RefundBillInfoBuilder refundReason(String refundReason) {
            this.refundReason=refundReason;
            return this;
        }

        public RefundBillInfoBuilder id(Integer id) {
            this.id=id;
            return this;
        }

        public RefundBillInfoBuilder refundCurrency(String refundCurrency) {
            this.refundCurrency=refundCurrency;
            return this;
        }

        public RefundBillInfoBuilder operatorId(String operatorId) {
            this.operatorId=operatorId;
            return this;
        }

        public RefundBillInfoBuilder orderId(Integer orderId) {
            this.orderId=orderId;
            return this;
        }

        public RefundBillInfoBuilder totalAmount(Double totalAmount) {
            this.totalAmount=totalAmount;
            return this;
        }

        public RefundBillInfoBuilder refundFee(Double refundFee) {
            this.refundFee=refundFee;
            return this;
        }

        public RefundBillInfoBuilder payType(PayType payType) {
            this.payType=payType;
            return this;
        }

        public RefundBillInfoBuilder gmtCreate(LocalDateTime gmtCreate) {
            this.gmtCreate=gmtCreate;
            return this;
        }

        public RefundBillInfoBuilder gmtPayment(LocalDateTime gmtPayment) {
            this.gmtPayment=gmtPayment;
            return this;
        }

        public RefundBillInfoBuilder alipayTradeNo(String alipayTradeNo) {
            this.alipayTradeNo=alipayTradeNo;
            return this;
        }

        public RefundBillInfoBuilder outBizNo(String outBizNo) {
            this.outBizNo=outBizNo;
            return this;
        }

        public RefundBillInfoBuilder buyerId(String buyerId) {
            this.buyerId=buyerId;
            return this;
        }

        public RefundBillInfoBuilder sellerId(String sellerId) {
            this.sellerId=sellerId;
            return this;
        }

        public RefundBillInfo build() {
            RefundBillInfo refundBillInfo=new RefundBillInfo();
            refundBillInfo.setId(id);
            refundBillInfo.setOrderId(orderId);
            refundBillInfo.setTotalAmount(totalAmount);
            refundBillInfo.setPayType(payType);
            refundBillInfo.setGmtCreate(gmtCreate);
            refundBillInfo.setGmtPayment(gmtPayment);
            refundBillInfo.setAlipayTradeNo(alipayTradeNo);
            refundBillInfo.setOutBizNo(outBizNo);
            refundBillInfo.setBuyerId(buyerId);
            refundBillInfo.setSellerId(sellerId);
            refundBillInfo.refundFee=this.refundFee;
            refundBillInfo.refundReason=this.refundReason;
            refundBillInfo.refundCurrency=this.refundCurrency;
            refundBillInfo.operatorId=this.operatorId;
            return refundBillInfo;
        }
    }
}
