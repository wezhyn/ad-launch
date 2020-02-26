package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.dto.AppTradeNotification;
import com.ad.admain.pay.*;
import com.wezhyn.project.AbstractBaseMapAdapterModel;
import com.wezhyn.project.controller.ResponseResult;
import com.wezhyn.project.utils.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author wezhyn
 * @since 11.22.2019
 */
@RestController
@RequestMapping("/callback")
public class ZfbController {


    @Autowired
    private ZfbTradeI zfbTrade;


    @PostMapping("/zfb/notify_url")
    public String verify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> parsedMap=AliPayHolder.sraCheck(request.getParameterMap());
        AlipayAsyncNotificationGetterI notificationGetter=AbstractBaseMapAdapterModel.createInstance(
                parsedMap, AlipayAsyncNotificationGetterI.class, true);
        boolean isVerify=notificationGetter.isVerify();

        boolean isHandle=isVerify ? new ZfbTradeResolver(zfbTrade).handle(notificationGetter) : isVerify;
        return isHandle ? "success" : "fail";
    }


    /**
     * 支付宝 app 支付同步通知,（忽略执行校验)
     * 无用
     *
     * @param request      request
     * @param httpResponse response
     * @return result
     */
//    @PostMapping("/app/notify_url")
    public ResponseResult verityApp(HttpServletRequest request, HttpServletRequest httpResponse) {
        AppTradeNotification appTradeNotification;
        try (InputStream jsonInput=request.getInputStream()) {
            String appNotificationStr=StreamUtils.copyToString(jsonInput, StandardCharsets.UTF_8);
            JSONObject appNotification=new JSONObject(appNotificationStr);
            AppTradeNotification.AppTradeNotificationBuilder notifyBuilder=AppTradeNotification.builder();
            String memo=getStrIgnoreError(appNotification, "memo");
            TradeStatus resultStatus=getTradeStatus(appNotification);
            if (resultStatus!=TradeStatus.TRADE_SUCCESS) {
                return ResponseResult.forFailureBuilder()
                        .withMessage(resultStatus.getValue()).build();
            }
            String resultStr=appNotification.getString("result");
            JSONObject resultNotification=new JSONObject(resultStr);
            notifyBuilder
                    .code(getStrIgnoreError(resultNotification, "code"))
                    .msg(getStrIgnoreError(resultNotification, "msg"))
                    .appId(getStrIgnoreError(resultNotification, "app_id"))
                    .sellerId(getStrIgnoreError(resultNotification, "seller_id"))

                    .outTradeNo(resultNotification.getString("trade_no"))
                    .totalAmount(resultNotification.getDouble("total_amount"))
                    .sign(resultNotification.getString("sign"))
                    .signType(resultNotification.getString("sign_type"));
            appTradeNotification=notifyBuilder.build();
        } catch (JSONException|IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getStrIgnoreError(JSONObject object, String param) {
        try {
            return object.getString(param);
        } catch (JSONException ignore) {
            return "";
        }
    }

    private TradeStatus getTradeStatus(JSONObject object) {
        try {
            int resultStatus=object.getInt("resultStatus");
            return EnumUtils.valueOfNumberEnum(TradeStatus.class, resultStatus);
        } catch (JSONException e) {
            return TradeStatus.TRADE_CANCEL_OTHER;
        }
    }

    private Integer getIntIgnoreError(JSONObject object, String param) {
        try {
            return object.getInt(param);
        } catch (JSONException ignore) {
            return -1;
        }
    }

}
