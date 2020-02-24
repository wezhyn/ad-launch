package com.ad.admain.controller.pay;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.pay.to.OrderVerify;
import com.ad.admain.controller.pay.to.TransferOrder;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.WithDrawNotification;
import com.ad.admain.pay.exception.WithdrawException;
import com.ad.admain.security.AdAuthentication;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@RestController
@RequestMapping("/api/pay")
public class PayController {

    @Autowired
    private GenericUserService userService;

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

    @PostMapping("/withdraw")
    public ResponseResult withdraw(@RequestBody JSONObject object, @AuthenticationPrincipal AdAuthentication authentication) throws WithdrawException {
        Double money=object.getDouble("withdraw");
        if (!checkSufficient()) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("收益不足").build();
        }
        TransferOrder order=TransferOrder.builder(money, userService.getById(authentication.getId()).get())
                .verify(OrderVerify.PASSING_VERIFY)
                .identify(identify())
                .identifyName(identifyName())
                .identityType("ALIPAY_LOGON_ID")
                .remark(String.format("%s -test", LocalDateTime.now()))
                .build();
        order.setId(ThreadLocalRandom.current().nextInt(100000));
        final WithDrawNotification response=AliPayHolder.handleWithDraw(order, TRANSFER_MODEL_MAPPER);
        if (response.isSuccess()) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("提现成功").build();
        } else {
            return ResponseResult.forFailureBuilder()
                    .withMessage(response.errCode().getSubMsg())
                    .build();
        }

    }


    private boolean checkSufficient() {
        return true;
    }

    private String identifyName() {
        return "avsftl2208@sandbox.com";
    }

    private String identify() {
        return "nctpdw6922@sandbox.com";
    }
}