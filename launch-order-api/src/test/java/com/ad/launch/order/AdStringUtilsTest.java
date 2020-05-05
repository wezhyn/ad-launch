package com.ad.launch.order;

import org.junit.Test;

/**
 * @author wezhyn
 * @since 05.05.2020
 */
public class AdStringUtilsTest {

    @Test
    public void gb2312Code() {
        System.out.println(AdStringUtils.gb2312Code("test-12"));
        System.out.println(AdStringUtils.gb2312Code("test-12").length());
        System.out.println("test-12".length());
        System.out.println(AdStringUtils.gb2312Code("ZUST显示设备"));
        System.out.println("ZUST显示设备".length());
    }
}