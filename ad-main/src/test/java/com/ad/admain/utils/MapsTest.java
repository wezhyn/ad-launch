package com.ad.admain.utils;

import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

/**
 * @author wezhyn
 * @since 12.02.2019
 */
public class MapsTest {

    @Test
    public void populateBean() {
        Map<String, String> readMap=ImmutableMap.of("businessParams", "test");

        AlipayTradeAppPayModel model=Maps.populateBean(readMap, AlipayTradeAppPayModel.class);
        System.out.println(model.getBusinessParams());
    }
}