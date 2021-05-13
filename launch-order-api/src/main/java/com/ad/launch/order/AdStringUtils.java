package com.ad.launch.order;

import java.io.UnsupportedEncodingException;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public class AdStringUtils {


    /**
     * 将 12 转成 0012
     *
     * @param number 12
     * @param length 4
     * @return 0012
     */
    public static String getNum(int number, int length) {
        String v=String.valueOf(number);
        StringBuilder sb=new StringBuilder();
        for (int i=0; i < length - v.length(); i++) {
            sb.append("0");
        }
        sb.append(v);
        return sb.toString();
    }


    public static String gb2312Code(String str) {
        final byte[] buf;
        try {
            buf=str.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        StringBuilder sb=new StringBuilder();
        for (byte b : buf) {
            int a;
            if (b < 127 && b > 0) {
                a = b;
            } else {
                a = 256 + b;
            }
            sb.append(Integer.toHexString(a).toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String code = gb2312Code("中国");
        System.out.println(code);
    }
}
