package com.ad.admain.controller.pay.to;

import com.ad.admain.controller.account.entity.IUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@Entity(name="ad_transfer_order")
@Setter
@Getter
public class TransferOrder extends Order {

    private String orderName;

    /**
     * 转账的唯一标识
     */
    private String identify;

    /**
     * 参与方的标识类型，目前支持如下类型：
     * 1、ALIPAY_USER_ID 支付宝的会员 ID
     * 2、ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式
     */
    private String identityType;
    /**
     * 参与方真实姓名，如果非空，将校验收款支付宝账号姓名一致性。
     */
    private String identifyName;

    /**
     * 业务备注
     */
    private String remark;


    public static TransferOrderBuilder builder(Double totalAmount, IUser user) {
        return new TransferOrderBuilder(totalAmount, user);
    }

    public static final class TransferOrderBuilder {
        private String orderName;
        private String identify;
        private String identityType;
        private String identifyName;
        private String remark;
        private Integer id;
        private Double totalAmount;
        private OrderVerify verify;

        private TransferOrderBuilder(Double totalAmount, IUser user) {
            totalAmount(totalAmount)
                    .orderName(user);
        }




        public TransferOrderBuilder identify(String identify) {
            this.identify=identify;
            return this;
        }

        public TransferOrderBuilder identityType(String identityType) {
            this.identityType=identityType;
            return this;
        }

        private void orderName(IUser user) {
            Assert.notNull(totalAmount, "账单金额不能为null");
            Assert.notNull(user, "用户不能为空");
            this.orderName=String.format("%s 提现 %f 钱 %s ", user.getUsername(), totalAmount, LocalDateTime.now());
        }

        public TransferOrderBuilder identifyName(String identifyName) {
            this.identifyName=identifyName;
            return this;
        }

        public TransferOrderBuilder remark(String remark) {
            this.remark=remark;
            return this;
        }


        private TransferOrderBuilder totalAmount(Double totalAmount) {
            this.totalAmount=totalAmount;
            return this;
        }

        public TransferOrderBuilder verify(OrderVerify verify) {
            this.verify=verify;
            return this;
        }

        public TransferOrder build() {
            TransferOrder transferOrder=new TransferOrder();
            transferOrder.setOrderName(orderName);
            transferOrder.setId(id);
            transferOrder.setTotalAmount(totalAmount);
            transferOrder.setVerify(verify);
            transferOrder.identifyName=this.identifyName;
            transferOrder.identify=this.identify;
            transferOrder.identityType=this.identityType;
            transferOrder.remark=this.remark;
            return transferOrder;
        }
    }
}
