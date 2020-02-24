package com.ad.admain.to;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.BillInfoService;
import com.ad.admain.controller.pay.to.AdBillInfo;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdBillInfoTest {


    @Autowired
    private AdOrderService orderService;
    @Autowired
    private BillInfoService orderInfoService;

    @Test
    public void saveOrderInfo() {
        Value value=new Value("test");
        AdOrder order=new AdOrder()
                .setPrice(100D)
                .setValueList(Collections.singletonList(value))
                .setRate(5);
        order=orderService.save(order);
        AdBillInfo orderInfo=AdBillInfo.builder()
                .orderId(order.getId())
                .build();
        orderInfoService.save(orderInfo);
    }
}