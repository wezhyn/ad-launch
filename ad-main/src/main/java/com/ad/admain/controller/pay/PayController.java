package com.ad.admain.controller.pay;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.pay.dto.RefundOrderDto;
import com.ad.admain.controller.pay.to.*;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.WithDrawNotification;
import com.ad.admain.pay.exception.WithdrawException;
import com.ad.admain.security.AdAuthentication;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@RestController
@RequestMapping("/api/pay")
public class PayController {

    @Autowired
    private GenericUserService userService;

    private static final Function<AdOrder, AlipayTradeAppPayModel> ORDER_ALIPAY_MAPPER=o->{
        AlipayTradeAppPayModel model=new AlipayTradeAppPayModel();
        String body=o.getProduceContext().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
        model.setBody(body);
        model.setSubject("ad-order-" + o.getId() + o.getLatitude() + ":" + o.getLongitude());
        model.setTotalAmount(String.valueOf(o.getPrice()*o.getNum()));
        model.setOutTradeNo(String.valueOf(o.getId()));
        model.setProductCode("QUICK_MSECURITY_PAY");
        return model;
    };


    @Autowired
    private BillInfoService billInfoService;
    @Autowired
    private AdOrderService orderService;


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

    @RequestMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult payOrder(
            @PathVariable(name="id") Integer orderId, @AuthenticationPrincipal AdAuthentication authentication) {
        final Optional<AdOrder> userOrder=orderService.findUserOrder(orderId, authentication.getId());
        return userOrder.map(o->{
            billInfoService.createOrder(o, PayType.ALI_PAY);
            final String sign=AliPayHolder.signZfb(o, ORDER_ALIPAY_MAPPER);
            return ResponseResult.forSuccessBuilder()
                    .withMessage("请支付")
                    .withData("sign", sign)
                    .build();
        }).orElseGet(()->{
            return ResponseResult.forFailureBuilder()
                    .withMessage("订单不存在")
                    .build();
        });
    }


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
