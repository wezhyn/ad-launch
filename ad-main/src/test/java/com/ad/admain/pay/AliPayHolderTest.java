package com.ad.admain.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.junit.Test;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public class AliPayHolderTest {

    @Test
    public void str() throws AlipayApiException {
        AlipayTradeAppPayRequest createRequest=new AlipayTradeAppPayRequest();

        AlipayTradeAppPayModel model=new AlipayTradeAppPayModel();
        String body="Test";
        model.setBody(body);
        model.setSubject("test-subject");
        model.setTotalAmount("100");
        model.setOutTradeNo("1");
        model.setProductCode("QUICK_MSECURITY_PAY");
        createRequest.setBizModel(model);
        createRequest.setNotifyUrl(AliPayProperties.CALLBACK_NOTIFY_URL);
        //这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response=AliPayHolder.ALI_PAY_CLIENT.sdkExecute(createRequest);
        if (response.isSuccess()) {
            System.out.println(response);
        }
    }


    @Test
    public void refund() throws AlipayApiException {
        AlipayTradeRefundRequest request=new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        model.setOutTradeNo("74");
        model.setTradeNo("2020022422001406181000040391");
        model.setRefundAmount("10");
        model.setRefundReason("测试用例");
        model.setOutRequestNo("H1H1H2");
        request.setBizModel(model);
        AlipayTradeRefundResponse response=AliPayHolder.ALI_PAY_CLIENT.certificateExecute(request);
        if (response.isSuccess()) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }

    @Test
    public void transferAccount() throws AlipayApiException {
        AlipayFundTransUniTransferRequest request=new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model=new AlipayFundTransUniTransferModel();
        model.setOutBizNo("123123345");
        model.setTransAmount("1111");
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setOrderTitle("test");
        model.setBizScene("DIRECT_TRANSFER");
        Participant payee=new Participant();
        payee.setIdentity("nctpdw6922@sandbox.com");
        payee.setIdentityType("ALIPAY_LOGON_ID");
        payee.setName("沙箱环境");
        model.setPayeeInfo(payee);
        model.setRemark("test");
        request.setBizModel(model);
        AlipayFundTransUniTransferResponse response=AliPayHolder.ALI_PAY_CLIENT.certificateExecute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}