package com.ad.admain.utils;

import java.util.List;

/**
 * @ClassName StringUtils
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/18 10:24
 * @Version 1.0
 */
public class StringUtils {
    public static final String rawCron="0 0/5 * * *？";
    public static final int carRate=10;

    public static String getContent(String rawContent) {
        String content="";
        String[] part=rawContent.split("#");
        for (int i=0; i < part.length; i++) {
            content+=part[i];
        }
        return content;
    }

    //获取List参数值
    public static String getListString(List<String> list) {
        StringBuilder result=new StringBuilder();
        for (String s : list) {
            result.append(s).append(" ");
        }
        return result.toString();
    }


    public static void main(String[] args) {
        System.out.println(getContent("hello#world#new#test"));
    }
}
