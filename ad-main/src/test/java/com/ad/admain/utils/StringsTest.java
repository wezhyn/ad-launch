package com.ad.admain.utils;

import org.junit.Test;

/**
 * @author wezhyn
 * @since 12.02.2019
 */
public class StringsTest {


    @Test
    public void extraUnderline() {
        String s="private String notify_time;\n" +
                "private String notify_type;\n" +
                "private String notify_id;\n" +
                "private String app_id;\n" +
                "private String charset;\n" +
                "private String version;\n" +
                "private String sign_type;\n" +
                "private String sign;\n" +
                "private String trade_no;\n" +
                "private String out_trade_no;\n" +
                "private String out_biz_no;\n" +
                "private String buyer_id;\n" +
                "private String buyer_logon_id;\n" +
                "private String seller_id;\n" +
                "private String seller_email;\n" +
                "private String trade_status;\n" +
                "private String total_amount;\n" +
                "private String receipt_amount;\n" +
                "private String invoice_amount;\n" +
                "private String buyer_pay_amount;\n" +
                "private String point_amount;\n" +
                "private String refund_fee;\n" +
                "private String subject;\n" +
                "private String body;\n" +
                "private String gmt_create;\n" +
                "private String gmt_payment;\n" +
                "private String gmt_refund;\n" +
                "private String gmt_close;\n" +
                "private String fund_bill_list;\n" +
                "private String passback_params;\n" +
                "private String voucher_detail_list;";
        System.out.println(Strings.underlineNameToPropertyName(s));
    }

    @Test
    public void extractGetterAndSetterProperty() {
    }

    @Test
    public void underline() {
        System.out.println(Strings.underlineNameToPropertyName("full_name"));
        System.out.println(Strings.underlineNameToPropertyName("fullName"));
    }

    @Test
    public void findFirstUpperCharacter() {
        System.out.println(Strings.extractGetterAndSetterProperty("getFullName"));
    }
}