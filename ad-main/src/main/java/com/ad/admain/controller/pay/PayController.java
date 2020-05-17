package com.ad.admain.controller.pay;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.pay.dto.RefundOrderDto;
import com.ad.admain.controller.pay.to.*;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.PayException;
import com.ad.admain.pay.RefundNotification;
import com.ad.admain.pay.WithDrawNotification;
import com.ad.admain.pay.exception.WithdrawException;
import com.ad.admain.security.AdAuthentication;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.Participant;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
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
    private RefundBillInfoService refundBillInfoService;


    @Autowired
    private BillInfoService billInfoService;
    @Autowired
    private AdOrderService orderService;


    private static final Function<TransferOrder, AlipayFundTransUniTransferModel> TRANSFER_MODEL_MAPPER = o -> {
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
        }).orElseGet(() -> {
            return ResponseResult.forFailureBuilder()
                    .withMessage("订单不存在")
                    .build();
        });
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
                    throw new RuntimeException("未开动");
                }
                case EXECUTION_COMPLETED: {
                    throw new RuntimeException("订单已经结束");
                }
                case SUCCESS_PAYMENT: {
//                    第一次退款
                    final AdBillInfo orderInfo = billInfoService.getByOrderId(uo.getId()).get();
//                    并发控制委托 mysql
                    if (!orderService.modifyOrderStatus(uo.getId(), OrderStatus.REFUNDING)) {
                        throw new PayException("退款失败");
                    }
                    Assert.notNull(orderInfo, "系统异常");
                    RefundOrder refundOrder = RefundOrder.createRefundOrder(authentication.getId(), refundOrderDto.getRefundAmount(), refundOrderDto.getRefundReason())
                            .outTradeNo(orderInfo.getAlipayTradeNo())
                            .adOrderId(orderInfo.getOrderId())
                            .build();
                    try {
                        final RefundNotification refundResponse = AliPayHolder.refundAmount(refundOrder, ORDER_REFUND_ALIPAY_MAPPER);
                        if (refundResponse.isSuccess()) {
                            RefundBillInfo refundBill = RefundBillInfo.fromOrder(refundOrder)
                                    .build();
                            refundBillInfoService.save(refundBill);
                            orderService.modifyOrderStatus(uo.getId(), OrderStatus.REFUNDED);
                            return ResponseResult
                                    .forSuccessBuilder()
                                    .withMessage("退款成功")
                                    .build();
                        } else {
                            orderService.rollbackRefundOrderStatus(uo.getId(), originStatus);
                            return ResponseResult.forFailureBuilder()
                                    .withMessage(refundResponse.err())
                                    .build();
                        }
                    } catch (Exception e) {
                        orderService.rollbackRefundOrderStatus(uo.getId(), originStatus);
                        e.printStackTrace();
                        throw new PayException(e);
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
        }).orElseGet(() -> {
            return ResponseResult.forFailureBuilder()
                    .withMessage("退款失败")
                    .build();
        });
    }

    @PostMapping("/withdraw")
    public ResponseResult withdraw(@RequestBody JSONObject object, @AuthenticationPrincipal AdAuthentication
            authentication) throws WithdrawException {
        Double money = object.getDouble("withdraw");
        if (!checkSufficient()) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("收益不足").build();
        }
        TransferOrder order = TransferOrder.builder(money, userService.getById(authentication.getId()).get())
                .verify(OrderVerify.PASSING_VERIFY)
                .identify(identify())
                .identifyName(identifyName())
                .identityType("ALIPAY_LOGON_ID")
                .remark(String.format("%s -test", LocalDateTime.now()))
                .build();
        order.setId(ThreadLocalRandom.current().nextInt(100000));
        final WithDrawNotification response = AliPayHolder.handleWithDraw(order, TRANSFER_MODEL_MAPPER);
        if (response.isSuccess()) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("提现成功").build();
        } else {
            return ResponseResult.forFailureBuilder()
                    .withMessage(response.errCode().getSubMsg())
                    .build();
        }

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
