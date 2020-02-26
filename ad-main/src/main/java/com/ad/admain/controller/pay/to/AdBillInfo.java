package com.ad.admain.controller.pay.to;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.pay.TradeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由 {@link AdOrderService#save(Object)} 创建
 * 订单的支付情况，并保存 Alipay 的信息
 *
 * @author wezhyn
 * @since 12.01.2019
 */
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@Data
@DynamicInsert
@Entity(name="ad_bill_info")
@Table(indexes={
        @Index(name="bill_info_id_total_amount", columnList="gmtPayment,tradeStatus,totalAmount")
})
public class AdBillInfo extends BillInfo {


    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id", referencedColumnName="id", insertable=false, updatable=false)
    private AdOrder order;


    public static AdBillInfoBuilder builder() {
        return new AdBillInfoBuilder();
    }

    public static final class AdBillInfoBuilder {
        private Integer id;
        private Integer orderId;
        private TradeStatus tradeStatus;
        private Double totalAmount;
        private AdOrder order;
        private PayType payType;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtPayment;
        private String alipayTradeNo;
        private String outBizNo;
        private String buyerId;
        private String sellerId;

        private AdBillInfoBuilder() {
        }

        public static AdBillInfoBuilder anAdBillInfo() {
            return new AdBillInfoBuilder();
        }

        public AdBillInfoBuilder id(Integer id) {
            this.id=id;
            return this;
        }

        public AdBillInfoBuilder orderId(Integer orderId) {
            this.orderId=orderId;
            return this;
        }

        public AdBillInfoBuilder tradeStatus(TradeStatus tradeStatus) {
            this.tradeStatus=tradeStatus;
            return this;
        }

        public AdBillInfoBuilder totalAmount(Double totalAmount) {
            this.totalAmount=totalAmount;
            return this;
        }

        public AdBillInfoBuilder order(AdOrder order) {
            this.order=order;
            return this;
        }

        public AdBillInfoBuilder payType(PayType payType) {
            this.payType=payType;
            return this;
        }

        public AdBillInfoBuilder gmtCreate(LocalDateTime gmtCreate) {
            this.gmtCreate=gmtCreate;
            return this;
        }

        public AdBillInfoBuilder gmtPayment(LocalDateTime gmtPayment) {
            this.gmtPayment=gmtPayment;
            return this;
        }

        public AdBillInfoBuilder alipayTradeNo(String alipayTradeNo) {
            this.alipayTradeNo=alipayTradeNo;
            return this;
        }

        public AdBillInfoBuilder outBizNo(String outBizNo) {
            this.outBizNo=outBizNo;
            return this;
        }

        public AdBillInfoBuilder buyerId(String buyerId) {
            this.buyerId=buyerId;
            return this;
        }

        public AdBillInfoBuilder sellerId(String sellerId) {
            this.sellerId=sellerId;
            return this;
        }

        public AdBillInfo build() {
            AdBillInfo adBillInfo=new AdBillInfo();
            adBillInfo.setId(id);
            adBillInfo.setOrderId(orderId);
            adBillInfo.setTradeStatus(tradeStatus);
            adBillInfo.setTotalAmount(totalAmount);
            adBillInfo.setOrder(order);
            adBillInfo.setPayType(payType);
            adBillInfo.setGmtCreate(gmtCreate);
            adBillInfo.setGmtPayment(gmtPayment);
            adBillInfo.setAlipayTradeNo(alipayTradeNo);
            adBillInfo.setOutBizNo(outBizNo);
            adBillInfo.setBuyerId(buyerId);
            adBillInfo.setSellerId(sellerId);
            return adBillInfo;
        }
    }
}
