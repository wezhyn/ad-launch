package com.ad.admain.message.api.impl;

import com.ad.admain.message.response.EasemobUserResponse;
import com.google.gson.Gson;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class EasemobIMUsersTest {


    @Test
    public void createNewIMUserSingle() {
        RegisterUsers registerUsers=new RegisterUsers();
        User user=new User()
                .username("wezhyn-test1")
                .password("123123");
        registerUsers.add(user);
        String result=(String) new EasemobIMUsers().createNewIMUserSingle(registerUsers);
        Gson gson=new Gson();
        EasemobUserResponse response=gson.fromJson(result, EasemobUserResponse.class);
        log.info(result);
        log.info(response.toString());
    }

    @Test
    public void getMessage() {
        EasemobChatMessage easemobChatMessage=new EasemobChatMessage();
        String result=(String) easemobChatMessage.exportChatMessages("2019092718");
        System.out.println(result);

    }
}