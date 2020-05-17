package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.controller.pay.to.PayType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RefundBillInfoServiceImplTest {

    @Autowired
    private RefundBillInfoServiceImpl refundBillInfoService;

    @Test
    public void refund() {
        refundBillInfoService.refund(45, 1, OrderStatus.SUCCESS_PAYMENT, 10D, "test", PayType.ALI_PAY);
    }
}