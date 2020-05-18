package com.ad.admain.controller.pay;

import com.ad.admain.controller.account.impl.SocialUserService;
import com.ad.admain.controller.account.user.SocialType;
import com.ad.admain.controller.account.user.SocialUser;
import com.ad.admain.controller.pay.to.*;
import com.ad.admain.mq.order.CheckOrderStatueProduceI;
import com.ad.admain.mq.order.UserAuthMessage;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static com.ad.admain.controller.pay.PayController.ORDER_ALIPAY_MAPPER;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("isAuthenticated()")
public class AuthController {

    private static final AdProduce authProduce = AdProduce.builder()
            .produceContext(Collections.singletonList("认证支付"))
            .price(0.01)
            .num(1)
            .latitude(0.0)
            .longitude(0.0)
            .vertical(false)
            .scope(0.0)
            .rate(0)
            .startDate(LocalDate.of(2020, 1, 1))
            .endDate(LocalDate.of(2020, 1, 1))
            .startTime(LocalTime.MIN)
            .endTime(LocalTime.MIN)
            .deliverNum(1)
            .build();
    @Autowired
    private SocialUserService socialUserService;
    @Autowired
    private BillInfoService billInfoService;
    @Autowired
    private CheckOrderStatueProduceI checkOrderStatueProduce;
    @Autowired
    private AdOrderService orderService;


    @GetMapping("/info")
    public ResponseResult authInfo(@AuthenticationPrincipal AdAuthentication adAuthentication) {
        final SocialUser user = socialUserService.getUser(adAuthentication.getId(), SocialType.ALIPAY);
        if (user != null) {
            return ResponseResult.forSuccessBuilder()
                    .withData("alipayId", user.getSocialAccountId())
                    .withData("username", user.getOrderUser().getUsername())
                    .withData("bindTime", user.getRegisterTime())
                    .withData("status", true)
                    .build();
        } else {
            return ResponseResult.forSuccessBuilder()
                    .withData("status", false)
                    .withMessage("无认证信息").build();
        }
    }

    @PostMapping("/alipay")
    public ResponseResult alipayAuth(@AuthenticationPrincipal AdAuthentication adAuthentication) throws CloneNotSupportedException {
        if (!socialUserService.isAuth(adAuthentication.getId(), SocialType.ALIPAY)) {
            AdOrder authOrder = (AdOrder) new AdOrder()
                    .setOrderStatus(OrderStatus.WAITING_PAYMENT)
                    .setProduce(authProduce.clone())
                    .setVerify(OrderVerify.PASSING_VERIFY)
                    .setIsDelete(true)
                    .setUid(adAuthentication.getId())
                    .setTotalAmount(0.01);
            final AdOrder adOrder = orderService.save(authOrder);
            billInfoService.getOrCreateOrder(adOrder, PayType.ALI_PAY);
            checkOrderStatueProduce.authOrder(new UserAuthMessage(adOrder.getId(), adAuthentication.getId()));
            final String sign = AliPayHolder.signZfb(adOrder, ORDER_ALIPAY_MAPPER);
            return ResponseResult.forSuccessBuilder()
                    .withMessage("请支付")
                    .withData("sign", sign)
                    .build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("已经认证过")
                .build();
    }

    @GetMapping("/sign")
    public ResponseResult signLogin(@AuthenticationPrincipal AdAuthentication adAuthentication) {
        String s = AliPayHolder.getLoginSign(adAuthentication.getId());
        return ResponseResult.forSuccessBuilder()
                .withData("sign", s)
                .build();
    }
}
