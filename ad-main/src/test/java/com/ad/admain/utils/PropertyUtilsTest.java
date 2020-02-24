package com.ad.admain.utils;

import com.ad.admain.controller.account.AuthenticationEnum;
import com.ad.admain.controller.account.SexEnum;
import com.ad.admain.controller.account.entity.GenericUser;
import com.wezhyn.project.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static com.wezhyn.project.utils.PropertyUtils.copyProperties;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class PropertyUtilsTest {


    @Test
    public void sex() {
        GenericUser newObject=GenericUser.builder()
                .sex(SexEnum.MALE).build();
        GenericUser oldObject=GenericUser.builder()
                .sex(SexEnum.UNKNOWN).build();
        PropertyUtils.copyProperties(newObject, oldObject);

        System.out.println(oldObject.getSex());
    }

    @Test
    public void copyPropertiesTest() {
        GenericUser target=new GenericUser();
        target.setRealName("兆兆");
        target.setUsername("zhaoo");
        target.setPassword("wezhyn");
        target.setSex(SexEnum.MALE);
        target.setRoles(AuthenticationEnum.CUSTOMER);
        log.info("sourceUser: {}", target.toString());

        GenericUser source=new GenericUser();
        source.setRealName("dzj");
        source.setSex(SexEnum.MALE);
        source.setPassword("passsword");
        source.setRoles(AuthenticationEnum.CUSTOMER);
        source.setEmail("fuckyou@dzj.com");
        log.info("targetUser :{}", source);
        copyProperties(source, target);

        log.info("target after copy :{}", source);
        log.info("source after copoy: {}", target);
    }
}