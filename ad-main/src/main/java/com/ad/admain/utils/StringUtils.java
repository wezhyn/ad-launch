package com.ad.admain.utils;

import java.util.Random;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public final class StringUtils {
    public static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for (int i=0; i < length; i++) {
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str==null || "".equals(str);
    }
}
