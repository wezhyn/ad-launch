package com.ad.admain.controller.pay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.pay.dto.RefundOrderDto;
import com.ad.admain.controller.pay.impl.RefundBillInfoServiceImpl;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.controller.pay.to.PayType;
import com.ad.admain.controller.pay.to.RefundOrder;
import com.ad.admain.controller.pay.to.TransferOrder;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.AliPayProperties;
import com.ad.admain.pay.AlipayAsyncNotificationGetterI;
import com.ad.admain.pay.PayException;
import com.ad.admain.pay.ZfbTradeI;
import com.ad.admain.pay.ZfbTradeResolver;
import com.ad.admain.security.AdAuthentication;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.Participant;
import com.wezhyn.project.AbstractBaseMapAdapterModel;
import com.wezhyn.project.controller.ResponseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@RestController
@RequestMapping("/api/pay")
public class PayController {

    @Autowired
    private GenericUserService userService;
    public static final Function<RefundOrder, AlipayTradeRefundModel> ORDER_REFUND_ALIPAY_MAPPER = b -> {
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(String.valueOf(b.getAdOrderId()));
        model.setTradeNo(b.getOutTradeNo());
        model.setRefundAmount(b.getTotalAmount().toString());
        model.setRefundCurrency("");
        model.setOutRequestNo(String.format("%s-refund-1", b.getUid()));
        model.setRefundReason(b.getRefundReason());
        model.setOperatorId("System");
        return model;
    };

    public static final Function<AdOrder, AlipayTradeAppPayModel> ORDER_ALIPAY_MAPPER = o -> {
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        String body = o.getProduceContext().stream()
            .filter(Objects::nonNull)
            .collect(Collectors.joining(","));
        model.setBody(body);
        model.setSubject("ad-order-" + o.getId() + o.getLatitude() + ":" + o.getLongitude());
        model.setTotalAmount(String.valueOf(o.getPrice() * o.getNum()));
        model.setOutTradeNo(String.valueOf(o.getId()));
        model.setProductCode("QUICK_MSECURITY_PAY");
        return model;
    };
    @Autowired
    private RefundBillInfoServiceImpl refundBillInfoService;

    @Autowired
    private BillInfoService billInfoService;
    @Autowired
    private AdOrderService orderService;
    @Autowired
    private ZfbTradeI zfbTrade;

    public static final Function<TransferOrder, AlipayFundTransUniTransferModel> TRANSFER_MODEL_MAPPER = o -> {
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(o.getId().toString());
        model.setTransAmount(o.getTotalAmount().toString());
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setOrderTitle(o.getOrderName());
        model.setBizScene("DIRECT_TRANSFER");
        Participant payee = new Participant();
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
        @PathVariable(name = "id") Integer orderId, @AuthenticationPrincipal AdAuthentication authentication) {
        final Optional<AdOrder> userOrder = orderService.findUserOrder(orderId, authentication.getId());
        return userOrder.map(o -> {
            billInfoService.getOrCreateOrder(o, PayType.ALI_PAY);
            final String sign = AliPayHolder.signZfb(o, ORDER_ALIPAY_MAPPER);
            return ResponseResult.forSuccessBuilder()
                .withMessage("请支付")
                .withData("sign", sign)
                .build();
        }).orElseGet(() -> ResponseResult.forFailureBuilder()
            .withMessage("订单不存在")
            .build());
    }

    @PostMapping("/mock")
    public ResponseResult verifyMock(String id, String totalAmount) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter mockTradeNo = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        Map<String, String> parsedMap = new HashMap<>();
        parsedMap.put("out_trade_no", id);

        String mockTime = mockTradeNo.format(LocalDateTime.now());
        parsedMap.put("trade_no", mockTime + RandomStringUtils.randomNumeric(20 - mockTime.length()));
        parsedMap.put("total_amount", totalAmount);
        parsedMap.put("app_id", AliPayProperties.APP_ID);
        parsedMap.put("buyer_id", "2088102179506184");
        parsedMap.put("seller_id", AliPayProperties.AD_SYSTEM_SELLER_ID);
        parsedMap.put("gmt_create", dateTimeFormatter.format(LocalDateTime.now()));
        parsedMap.put("gmt_payment", dateTimeFormatter.format(LocalDateTime.now()));

        AlipayAsyncNotificationGetterI notificationGetter = AbstractBaseMapAdapterModel.createInstance(
            parsedMap, AlipayAsyncNotificationGetterI.class, true);

        boolean isHandle = new ZfbTradeResolver(zfbTrade).handle(notificationGetter);
        return isHandle ? ResponseResult.forSuccessBuilder().withMessage("success").build() :
            ResponseResult.forFailureBuilder().withMessage("fail").build();
    }

    @PostMapping("/refund/{id}")
    public ResponseResult refundOrder(
        @PathVariable("id") Integer orderId,
        @RequestBody RefundOrderDto refundOrderDto,
        @AuthenticationPrincipal AdAuthentication authentication) {
        Assert.isTrue(orderId.equals(refundOrderDto.getAdOrderId()), "退款参数不正确");
        final Optional<AdOrder> userOrder = orderService.findUserOrder(orderId, authentication.getId());
        return userOrder.map(uo -> {
            if (refundOrderDto.getRefundAmount() > uo.getTotalAmount()) {
                throw new PayException("退款金额不能大于订单金额");
            }
            final OrderStatus originStatus = uo.getOrderStatus();
            switch (originStatus) {
                case EXECUTING:
                case WAITING_EXECUTION: {
                    throw new RuntimeException("订单等待分配中，不能退款");
                }
                case EXECUTION_COMPLETED: {
                    throw new RuntimeException("订单已经结束");
                }
                case SUCCESS_PAYMENT: {
                    //                    第一次退款
                    if (refundBillInfoService.refund(refundOrderDto.getAdOrderId(), authentication.getId(),
                        uo.getOrderStatus(),
                        refundOrderDto.getRefundAmount(), refundOrderDto.getRefundReason(), PayType.ALI_PAY)) {
                        return ResponseResult
                            .forSuccessBuilder()
                            .withMessage("退款成功")
                            .build();
                    } else {
                        return ResponseResult.forFailureBuilder()
                            .withMessage("请勿重复退款")
                            .build();
                    }
                }
                case REFUNDING: {
                    throw new PayException("请勿重复提交");
                }
                case WAITING_PAYMENT: {
                    throw new PayException("订单未支付");
                }
                case CANCEL: {
                    throw new PayException("订单被取消");
                }
                case REFUNDED: {
                    throw new PayException("订单已经是退款状态");
                }
                default: {
                    throw new PayException("暂未开动");
                }
            }
        }).orElseGet(() -> ResponseResult.forFailureBuilder()
            .withMessage("退款失败")
            .build());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult handler(Exception e) {
        return ResponseResult.forFailureBuilder()
            .withMessage(e.getMessage()).build();
    }

    private boolean checkSufficient() {
        return true;
    }

    private String identifyName() {
        return "puibgx4870@sandbox.com";
    }

    private String identify() {
        return "nctpdw6922@sandbox.com";
    }
}
