package com.ad.admain.controller.pay.to;

import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.controller.account.entity.IUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "Refund_order")
public class RefundOrder extends Order {


    /**
     * 广告订单编号
     */
    @Column(name = "ad_order_id")
    private Integer adOrderId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ad_order_id",insertable = false,updatable = false)
    private AdOrder adOrder;

    /**
     * 支付宝交易号
     */
    @Column(name = "trade_no")
    private String tradeNo;
    /**
     * 订单退款币种信息
     */
    @Column(name = "refund_currency")
    private String refundCurrency;
    /**
     * 退款的原因
     */
    @Column(name = "refund_reason")
    private String refundReason;

    /**
     * 商户的操作员编号
     */
    @Column(name = "operator_id")
    private String operatorId;


    public static final class RefundOrderBuilder {
        private Integer adOrderId;
        private AdOrder adOrder;
        private String tradeNo;
        private Integer id;
        private String refundCurrency;
        private GenericUser orderUser;
        private Integer uid;
        private String refundReason;
        private Double totalAmount;
        private String operatorId;
        private LocalDateTime createTime;
        private LocalDateTime modifyTime;
        private OrderVerify verify;

        private RefundOrderBuilder() {
        }

        public static RefundOrderBuilder aRefundOrder() {
            return new RefundOrderBuilder();
        }

        public RefundOrderBuilder withAdOrderId(Integer adOrderId) {
            this.adOrderId = adOrderId;
            return this;
        }

        public RefundOrderBuilder withAdOrder(AdOrder adOrder) {
            this.adOrder = adOrder;
            return this;
        }

        public RefundOrderBuilder withTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
            return this;
        }

        public RefundOrderBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public RefundOrderBuilder withRefundCurrency(String refundCurrency) {
            this.refundCurrency = refundCurrency;
            return this;
        }

        public RefundOrderBuilder withOrderUser(GenericUser orderUser) {
            this.orderUser = orderUser;
            return this;
        }

        public RefundOrderBuilder withUid(Integer uid) {
            this.uid = uid;
            return this;
        }

        public RefundOrderBuilder withRefundReason(String refundReason) {
            this.refundReason = refundReason;
            return this;
        }

        public RefundOrderBuilder withTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public RefundOrderBuilder withOperatorId(String operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public RefundOrderBuilder withCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public RefundOrderBuilder withModifyTime(LocalDateTime modifyTime) {
            this.modifyTime = modifyTime;
            return this;
        }

        public RefundOrderBuilder withVerify(OrderVerify verify) {
            this.verify = verify;
            return this;
        }

        public RefundOrder build() {
            RefundOrder refundOrder = new RefundOrder(adOrderId, adOrder, tradeNo, refundCurrency, refundReason, operatorId);
            refundOrder.setId(id);
            refundOrder.setOrderUser(orderUser);
            refundOrder.setUid(uid);
            refundOrder.setTotalAmount(totalAmount);
            refundOrder.setCreateTime(createTime);
            refundOrder.setModifyTime(modifyTime);
            refundOrder.setVerify(verify);
            return refundOrder;
        }
    }
}
