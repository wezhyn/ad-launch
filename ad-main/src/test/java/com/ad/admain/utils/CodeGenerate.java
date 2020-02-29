package com.ad.admain.utils;

import org.junit.Before;
import org.junit.Test;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
public class CodeGenerate {


    private String msg;

    @Before
    public void init() {

    }

    @Test
    public void handle() {
        final String[] lines=msg.split("\n");
        for (String s : lines) {
            final String[] strs=s.split("\t");
            System.out.printf("%s(\"%s\"),\n", strs[0].trim(), strs[1].trim());
        }
    }
}
