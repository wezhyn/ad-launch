package com.ad.message.api.impl;

import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wezhyn
 * @date 2019/09/25:20:42
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class EasemobIMUsersTest {

    @Test
    public void getIMUserByUserName() {
    }

    @Test
    public void modifyIMUserPasswordWithAdminToken() {
        RegisterUsers users=new RegisterUsers();
        User u=new User()
                .username("wezhynnnn")
                .password("weeeeeeeewww");
        users.add(u);
        new EasemobIMUsers().createNewIMUserSingle(users);
    }
}