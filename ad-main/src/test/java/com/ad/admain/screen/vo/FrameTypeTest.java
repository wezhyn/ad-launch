package com.ad.admain.screen.vo;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author wezhyn
 * @since 03.08.2020
 */
public class FrameTypeTest {

    @Test
    public void parse() {
        final FrameType parse=FrameType.parse('1');
        Assert.assertEquals(parse, FrameType.CONFIRM);

        final FrameType parse3=FrameType.parse('3');
        Assert.assertEquals(parse3, FrameType.GPS);
    }
}