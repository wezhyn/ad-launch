package com.ad.admain.utils;

import com.ad.admain.controller.account.AuthenticationEnum;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class RoleAuthenticationUtilsTest {

    @Test
    public void grantedAuthorities2String() {
        System.out.println(RoleAuthenticationUtils.forGrantedAuthorities(AuthenticationEnum.CUSTOMER));

    }

    @Test
    public void grantedAuthorities2SingleString() {
        for (int i=0; i < 1000; i++) {
            System.out.println(RoleAuthenticationUtils.grantedAuthorities2SingleString(
                    RoleAuthenticationUtils.CACHE.get(AuthenticationEnum.CUSTOMER)
            ));
        }
    }

    @Test
    public void authentication2ValueStringList() {
        for (int i=0; i < 1000; i++) {
            System.out.println(Arrays.toString(
                    RoleAuthenticationUtils.authentication2ValueStringList(
                            AuthenticationEnum.DEVELOPER
                    )
            ));
        }
    }
}