package com.ad.admain.controller;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class ISmsServiceTest {

    @Test
    public void isChinaPhoneLegal() {
        boolean isTrue=ISmsService.verifyPhone("13588761541");
        assertTrue(isTrue);
    }
}