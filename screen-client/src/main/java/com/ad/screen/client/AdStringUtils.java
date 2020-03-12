package com.ad.screen.client;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

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

    public static int parseNum(String number) {
        for (int i=0; i < number.length(); i++) {
            if (number.charAt(i)!='0') {
                return Integer.parseInt(number.substring(i));
            }
        }
        return 0;
    }

    public static String code2String(String code) {
        final byte[] buf=new byte[code.length()/2];
        for (int i=0; i < code.length(); i+=2) {
            int a;

            int b=Integer.valueOf(code.substring(i, i + 2), 16);
            if (b > 0 && b < 127) {
                a=b;
            } else {
                a=b - 256;
            }
            buf[i/2]=(byte) a;
        }
        return new String(buf, Charset.forName("gb2312"));
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
                a=b;
            } else {
                a=256 + b;
            }
            sb.append(Integer.toHexString(a).toUpperCase());
        }
        return sb.toString();
    }
}
