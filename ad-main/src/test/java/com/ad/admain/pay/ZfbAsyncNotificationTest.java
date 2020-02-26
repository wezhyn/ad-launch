package com.ad.admain.pay;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public class ZfbAsyncNotificationTest {
    @Test
    public void parseTime() {
        String time="2015-11-27 15:45:58";
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse=LocalDateTime.parse(time, dateTimeFormatter);
        System.out.println(parse.format(dateTimeFormatter));
    }

    @Test
    public void tradeStatus() {
        System.out.println(TradeStatus.valueOf("TRADE_CLOSED"));
    }


}