package com.ad.admain.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

/**
 * @author wezhyn
 * @since 11.22.2019
 */
public class DemoPay {


    public static void main(String[] args) throws AlipayApiException {
        AlipayClient alipayClient=new DefaultAlipayClient(
                AliPayProperties.SERVER_URL,
                AliPayProperties.APP_ID,
                AliPayProperties.APP_PRIVATE_KEY,
                "json",
                AliPayProperties.CHARSET,
                AliPayProperties.ALIPAY_PUBLIC_KEY,
                "RSA2");

        AlipayTradeAppPayRequest createRequest=new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel vv=null;
        createRequest.setBizModel(vv);
        AlipayTradeAppPayResponse response=alipayClient.sdkExecute(createRequest);
        if (response.isSuccess()) {
            System.out.println(response.getBody());
        }
    }


}
