package com.ad.admain.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wezhyn
 * @since 11.22.2019
 */
public final class ZfbPayHolder {


    private static AlipayClient ALIPAY_CLIENT;

    static {
        ALIPAY_CLIENT=new DefaultAlipayClient(
                ZfbPayProperties.SERVER_URL,
                ZfbPayProperties.APP_ID,
                ZfbPayProperties.APP_PRIVATE_KEY,
                "json",
                ZfbPayProperties.CHARSET,
                ZfbPayProperties.ALIPAY_PUBLIC_KEY,
                "RSA2");
    }

    /**
     * 支付宝验签,根据系统给定的基本配置进行验签
     * 通过 map.get(verify) true 则为验证通过
     *
     * @param zfbAsyncParams 支付宝异步通知验签
     * @return true: 验签成功
     */
    public static Map<String, String> sraCheck(Map<String, String[]> zfbAsyncParams) {
        Map<String, String> parseMap=new HashMap<>(17);
        if (zfbAsyncParams==null || zfbAsyncParams.size()==0) {
            parseMap.put("verify", "false");
            return parseMap;
        }
        for (String name : zfbAsyncParams.keySet()) {
            String[] values=zfbAsyncParams.get(name);
            String valueStr=Stream.of(values)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(","));
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            parseMap.put(name, valueStr);
        }
        System.out.println(parseMap);
        boolean isVerify;
        try {
            isVerify=AlipaySignature.rsaCheckV1(parseMap, ZfbPayProperties.ALIPAY_PUBLIC_KEY,
                    ZfbPayProperties.CHARSET, ZfbPayProperties.SIGN_TYPE);
        } catch (AlipayApiException e) {
            isVerify=false;
        }
        parseMap.put("verify", String.valueOf(isVerify));
        return parseMap;
    }

    public static <T, R extends AlipayTradeAppPayModel> String signZfb(T signOrder, Function<T, R> orderMapper) {
        try {
            AlipayTradeAppPayRequest createRequest=new AlipayTradeAppPayRequest();

            createRequest.setBizModel(orderMapper.apply(signOrder));
            createRequest.setNotifyUrl(ZfbPayProperties.CALLBACK_NOTIFY_URL);
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response=ALIPAY_CLIENT.sdkExecute(createRequest);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            if (response.isSuccess()) {
                return response.getBody();
            } else {
                throw new PayException("无法创建订单签名");
            }
        } catch (AlipayApiException e) {
            throw new PayException(e);
        }
    }
}
