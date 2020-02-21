package com.ad.admain.controller.dashboard;

import com.ad.admain.controller.dashboard.repository.BillAggregationRepository;
import com.ad.admain.controller.pay.TradeStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

/**
 * @author wezhyn
 * @since 01.22.2020
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class BillAggregationRepositoryTest {

    @Autowired
    private BillAggregationRepository billAggregationRepository;

    @Test
    public void query() {
        Map m=billAggregationRepository.getUserNumApproximate();
        System.out.println(m);
    }


    @Test
    public void save() {
        BillAggregation aggregation=new BillAggregation();
        aggregation.setBillScope(DateType.HOUR);
        aggregation.setBillSum(100D);
        billAggregationRepository.save(aggregation);
    }

    @Test
    public void account() {
        final BillAggregationRepository.BillAccount billAccount=billAggregationRepository.accountBillAccountUtilHour(1, TradeStatus.TRADE_SUCCESS,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), 0)));
        System.out.println(billAccount);
    }


}