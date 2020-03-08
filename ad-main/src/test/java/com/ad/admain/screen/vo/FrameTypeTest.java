package com.ad.admain.screen.vo;

import org.junit.Test;

/**
 * @author wezhyn
 * @since 03.08.2020
 */
public class FrameTypeTest {

    @Test
    public void parse() {
        final FrameType parse=FrameType.parse('1');
        System.out.println(parse);
    }
}