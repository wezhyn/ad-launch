package com.ad.admain.controller.pay;

import com.ad.admain.pay.TradeStatus;
import org.junit.Test;

/**
 * @author wezhyn
 * @since 12.03.2019
 */
public class TradeStatusTest {

    @Test
    public void test() {
        System.out.println(TradeStatus.valueOf("WAIT_BUYER_PAY"));
    }
}