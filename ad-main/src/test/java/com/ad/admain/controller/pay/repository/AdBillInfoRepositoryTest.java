package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.AdBillInfo;
import com.ad.admain.controller.pay.to.PayType;
import com.ad.admain.pay.TradeStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

/**
 * @author wezhyn
 * @since 01.01.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdBillInfoRepositoryTest {

    @Autowired
    private BillInfoRepository billInfoRepository;


    @Test
    public void get() {
        System.out.println(billInfoRepository.findById(7));
    }


    /**
     * 创建评估样品
     */
    @Test
    public void createSample() {
        final Random random=new Random();
        final Random datetime=new Random();
        for (int j=0; j < 10; j++) {
            for (int i=0; i < 24; i++) {
                int r=random.nextInt(10);
                for (int k=0; k < r; k++) {
                    AdBillInfo billInfo=AdBillInfo.builder()
                            .orderId(61)
                            .totalAmount(random.nextDouble()*200)
                            .tradeStatus(TradeStatus.TRADE_SUCCESS)
                            .alipayTradeNo(String.valueOf(random.nextInt(1000)))
                            .buyerId("111")
                            .gmtCreate(LocalDateTime.now())
                            .gmtPayment(LocalDateTime.of(LocalDate.now().minusDays(10 - j), LocalTime.MIN.plusHours(i)
                                    .plusMinutes(datetime.nextInt(10) + 10*k/2)))
                            .sellerId("123")
                            .payType(PayType.ALI_PAY)
                            .build();
                    billInfoRepository.save(billInfo);

                }
            }
        }

    }
}