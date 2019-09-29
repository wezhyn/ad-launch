package com.ad.admain.utils;

import com.ad.admain.dto.GenericUser;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static com.ad.admain.utils.PropertyUtils.copyProperties;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class PropertyUtilsTest {

    @Test
    public void copyPropertiesTest() {
        GenericUser sourceUser=new GenericUser();
        sourceUser.setRealName("兆兆");
        sourceUser.setUsername("zhaoo");
        sourceUser.setPassword("wezhyn");
        sourceUser.setSex(SexEnum.MALE);
        sourceUser.setRoles(AuthenticationEnum.CUSTOMER);
        log.info("sourceUser: {}", sourceUser.toString());

        GenericUser requestUser=new GenericUser();
        requestUser.setRealName("dzj");
        requestUser.setSex(SexEnum.MALE);
        requestUser.setPassword("passsword");
        requestUser.setRoles(AuthenticationEnum.CUSTOMER);
        requestUser.setEmail("fuckyou@dzj.com");
        log.info("targetUser :{}", requestUser);
        copyProperties(requestUser, sourceUser);

        log.info("target after copy :{}", requestUser);
        log.info("source after copoy: {}", sourceUser);
    }
}