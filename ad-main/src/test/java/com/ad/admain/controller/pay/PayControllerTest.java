package com.ad.admain.controller.pay;

import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.controller.account.entity.IUser;
import com.ad.admain.controller.pay.to.OrderVerify;
import com.ad.admain.controller.pay.to.TransferOrder;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.WithDrawNotification;
import com.ad.admain.pay.exception.WithdrawException;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.wezhyn.project.controller.ResponseResult;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
public class PayControllerTest {

    private static final Function<TransferOrder, AlipayFundTransUniTransferModel> TRANSFER_MODEL_MAPPER=o->{
        AlipayFundTransUniTransferModel model=new AlipayFundTransUniTransferModel();
        model.setOutBizNo(o.getId().toString());
        model.setTransAmount(o.getTotalAmount().toString());
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setOrderTitle(o.getOrderName());
        model.setBizScene("DIRECT_TRANSFER");
        Participant payee=new Participant();
        payee.setIdentity(o.getIdentify());
        payee.setIdentityType(o.getIdentityType());
        payee.setName(o.getIdentifyName());
        model.setPayeeInfo(payee);
        model.setRemark(o.getRemark());
        return model;
    };

    @Test
    public void withdraw() throws WithdrawException {
        ResponseResult responseResult;
        IUser user=GenericUser.builder()
                .username("wezhyn")
                .build();
        TransferOrder order=TransferOrder.builder(66D, user)
                .verify(OrderVerify.PASSING_VERIFY)
                .identify(identify())
                .identifyName(identifyName())
                .identityType("ALIPAY_LOGON_ID")
                .remark(String.format("%s -test", LocalDateTime.now()))
                .build();
        order.setId(ThreadLocalRandom.current().nextInt(100000));
        final WithDrawNotification response=AliPayHolder.handleWithDraw(order, TRANSFER_MODEL_MAPPER);
        if (response.isSuccess()) {
            responseResult=ResponseResult.forSuccessBuilder()
                    .withMessage("提现成功").build();
        } else {
            responseResult=ResponseResult.forFailureBuilder()
                    .withMessage(response.errCode().getSubMsg())
                    .build();
        }
        System.out.println(responseResult);
    }

    private boolean checkSufficient() {
        return true;
    }

    private String identifyName() {
        return "沙箱环境";
    }

    private String identify() {
        return "avsftl2208@sandbox.com";
    }
}