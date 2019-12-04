package com.ad.admain.pay;

import org.junit.Test;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public class ZfbPayHolderTest {

    @Test
    public void str() {
        String[] values=new String[]{"1", "2", "3"};
        String valueStr="";
        for (int i=0; i < values.length; i++) {
            valueStr=(i==values.length - 1) ? valueStr + values[i]
                    : valueStr + values[i] + ",";
        }
        System.out.println(valueStr);
        valueStr=Stream.of(values)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
        System.out.println(valueStr);
    }
}