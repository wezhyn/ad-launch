package com.ad.admain.utils;

/**
 * @ClassName StringUtils
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/18 10:24
 * @Version 1.0
 */
public class StringUtils {
    public static String getContent(String rawContent){
        String content = "";
        String[] part = rawContent.split("#");
        for (int i = 0; i < part.length; i++) {
            content+=part[i];
        }
        return content;
    }

    public static void main(String[] args) {
        System.out.println(getContent("hello#world#new#test"));
    }
}
