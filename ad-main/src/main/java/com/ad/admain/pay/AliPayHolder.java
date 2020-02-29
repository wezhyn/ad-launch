package com.ad.admain.pay;

import com.ad.admain.pay.exception.RefundException;
import com.ad.admain.pay.exception.WithdrawException;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

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
public final class AliPayHolder {


    public static AlipayClient ALI_PAY_CLIENT;

    static {
        switch (AliPayProperties.CERTIFICATE_TYPE) {
            case CERTIFICATE: {
                //构造client
                CertAlipayRequest certAlipayRequest=new CertAlipayRequest();
                certAlipayRequest.setServerUrl(AliPayProperties.SERVER_URL);
                certAlipayRequest.setPrivateKey(AliPayProperties.APP_PRIVATE_KEY);
                certAlipayRequest.setAppId(AliPayProperties.APP_ID);
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                certAlipayRequest.setCharset(AliPayProperties.CHARSET);
                certAlipayRequest.setSignType(AliPayProperties.SIGN_TYPE);
                certAlipayRequest.setCertPath(AliPayProperties.CERT_PATH);
                certAlipayRequest.setRootCertPath(AliPayProperties.ROOT_CERT_PATH);
                certAlipayRequest.setAlipayPublicCertPath(AliPayProperties.PUBLIC_CERT_PATH);
                try {
                    ALI_PAY_CLIENT=new DefaultAlipayClient(certAlipayRequest);
                } catch (AlipayApiException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case PRIVATE_KEY: {
                ALI_PAY_CLIENT=new DefaultAlipayClient(
                        AliPayProperties.SERVER_URL,
                        AliPayProperties.APP_ID,
                        AliPayProperties.APP_PRIVATE_KEY,
                        "json",
                        AliPayProperties.CHARSET,
                        AliPayProperties.ALIPAY_PUBLIC_KEY,
                        AliPayProperties.SIGN_TYPE
                );
                break;
            }
            default: {
                throw new RuntimeException("不支持的类型");
            }
        }
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
            parseMap.put(name, valueStr);
        }
        System.out.println(parseMap);
        boolean isVerify;
        try {

            isVerify=AlipaySignature.rsaCertCheckV1(parseMap, AliPayProperties.PUBLIC_CERT_PATH,
                    AliPayProperties.CHARSET, AliPayProperties.SIGN_TYPE);
        } catch (AlipayApiException e) {
            isVerify=false;
        }
        parseMap.put("verify", String.valueOf(isVerify));
        return parseMap;
    }

    /**
     * 订单请求
     *
     * @param signOrder   AdOrder
     * @param orderMapper AdOrder -> AlipayTradeAppPayModel
     * @param <T>         AdOrder
     * @param <R>         AlipayTradeAppPayModel
     * @return sign
     */
    public static <T, R extends AlipayTradeAppPayModel> String signZfb(T signOrder, Function<T, R> orderMapper) {
        try {
            AlipayTradeAppPayRequest createRequest=new AlipayTradeAppPayRequest();

            createRequest.setBizModel(orderMapper.apply(signOrder));
            createRequest.setNotifyUrl(AliPayProperties.CALLBACK_NOTIFY_URL);
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response=ALI_PAY_CLIENT.sdkExecute(createRequest);
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

    public static <T, R extends AlipayFundTransUniTransferModel> WithDrawNotification handleWithDraw(T order, Function<T, R> orderMapper) throws WithdrawException {
        final R model=orderMapper.apply(order);
        AlipayFundTransUniTransferRequest request=new AlipayFundTransUniTransferRequest();
        request.setBizModel(model);
        try {
            AlipayFundTransUniTransferResponse response=ALI_PAY_CLIENT.certificateExecute(request);
            return new WithDrawResponse(response);
        } catch (AlipayApiException e) {
            throw new WithdrawException(e);
        }

    }

    public static <T, R extends AlipayTradeRefundModel> RefundNotification refundAmount(T order, Function<T, R> orderMapper) throws RefundException {
        final R model=orderMapper.apply(order);
        AlipayTradeRefundRequest request=new AlipayTradeRefundRequest();
        request.setBizModel(model);
        try {
            AlipayTradeRefundResponse response=ALI_PAY_CLIENT.certificateExecute(request);
            return new RefundResponse(response);
        } catch (AlipayApiException e) {
            throw new RefundException(e);
        }

    }
}
