package com.ad.admain.controller;

import com.ad.admain.controller.account.dto.UserDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author wezhyn
 * @date 2019/09/28
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class UserControllerTest extends BaseControllerTest {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void register() throws Exception {
        UserDto genericUser=UserDto.builder()
                .username("wezhyn-register")
                .password("111111")
                .roles("customer")
                .avatar("123")
                .gender("male")
                .build();
        System.out.println(objectMapper.writeValueAsString(genericUser));
//        mockMvc.perform(post("/api/user/register")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(genericUser)))
//                .andExpect(status().isOk());

    }
}