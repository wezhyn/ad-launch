package com.ad.admain.convert;

import org.junit.Test;
import org.springframework.core.ResolvableType;

/**
 * @author wezhyn
 * @date 2019/10/07
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class TypeTest {

    @Test
    public void type() {

        ResolvableType userMapper=ResolvableType.forClass(GenericUserMapper.class)
                .as(AbstractMapper.class);

        System.out.println(userMapper.getType());
        System.out.println(userMapper.getSuperType());
    }
}
