package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.dto.RefundOrderDto;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.AliPayProperties;
import com.ad.admain.security.AdAuthentication;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @ClassName RefundController
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/26 15:46
 * @Version 1.0
 */
@Controller
@PreAuthorize("isAuthenticated()")
public class RefundController {

@Autowired
AdOrderService adOrderService;

    @RequestMapping("/refund")
    public ResponseResult refund(@RequestBody RefundOrderDto refundOrderDto,@AuthenticationPrincipal AdAuthentication authentication) throws AlipayApiException {
        Optional<AdOrder> optional = adOrderService.findUserOrder(refundOrderDto.getId(),authentication.getId());
        if (optional.get()==null){
            return ResponseResult.forFailureBuilder().withMessage("订单不存在").build();
        }
        else {
            AdOrder adOrder = new AdOrder();
            AlipayClient alipayClient = AliPayHolder.ALI_PAY_CLIENT;
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            model.setOutTradeNo(String.valueOf(refundOrderDto.getId()));
            model.setTradeNo(adOrder.getId().toString());
            model.setRefundAmount(refundOrderDto.getTotalAmount().toString());
            model.setRefundCurrency(refundOrderDto.getRefundCurrency());
            model.setRefundReason(refundOrderDto.getRefundReason());
            model.setOperatorId(refundOrderDto.getOperatorId());
            request.setBizModel(model);
            request.setNotifyUrl(AliPayProperties.CALLBACK_NOTIFY_URL);
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeRefundResponse response=alipayClient.certificateExecute(request);
            if (response.isSuccess()) {
                ResponseResult.forSuccessBuilder().withMessage("退款提交成功").build();
            } else {
                ResponseResult.forFailureBuilder().withMessage("退款提交失败").build();
            }
        }
        return ResponseResult.forFailureBuilder().withMessage("系统繁忙").build();
    }



}
