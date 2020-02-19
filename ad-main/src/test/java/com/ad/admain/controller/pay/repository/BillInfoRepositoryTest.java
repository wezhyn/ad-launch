package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.TradeStatus;
import com.ad.admain.controller.pay.to.BillInfo;
import com.ad.admain.controller.pay.to.PayType;
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
public class BillInfoRepositoryTest {

    @Autowired
    private BillInfoRepository billInfoRepository;


    @Test
    public void get() {
        System.out.println(billInfoRepository.findById(7));
    }


    @Test
    public void createSample() {
        final Random random=new Random();
        final Random datetime=new Random();
        final Random date=new Random();
        for (int j=0; j < 10; j++) {
            for (int i=0; i < 24; i++) {
                int r=random.nextInt(10);
                for (int k=0; k < r; k++) {
                    BillInfo billInfo=BillInfo.builder()
                            .orderId(62)
                            .totalAmount(random.nextDouble()*200)
                            .tradeStatus(TradeStatus.TRADE_SUCCESS)
                            .alipayTradeNo(String.valueOf(random.nextInt(1000)))
                            .buyerId("111")
                            .gmtCreate(LocalDateTime.now())
                            .gmtPayment(LocalDateTime.of(LocalDate.now().minusDays(10 - j), LocalTime.MIN.plusHours(i)
                                    .plusMinutes(datetime.nextInt(60))))
                            .sellerId("123")
                            .payType(PayType.ALIPAY)
                            .build();
                    billInfoRepository.save(billInfo);

                }
            }
        }

    }
}