package com.ad.admain.service.impl;

import com.ad.admain.message.api.IMUserAPI;
import com.ad.admain.message.api.impl.EasemobIMUsers;
import com.ad.admain.message.response.EasemobUserResponse;
import com.ad.admain.repository.EasemobRepository;
import com.ad.admain.service.AbstractBaseService;
import com.ad.admain.service.EasemobService;
import com.ad.admain.to.EasemobUser;
import com.google.gson.Gson;
import io.swagger.client.model.RegisterUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class EasemobServiceImpl extends AbstractBaseService<EasemobUser, Integer> implements EasemobService {


    @Autowired
    private EasemobRepository easemobRepository;
    private final static IMUserAPI IM_USER_API=new EasemobIMUsers();


    @Override
    public Optional<EasemobUser> registerEasemob(EasemobUser registerUser) {
        RegisterUsers registerUsers=new RegisterUsers();
        registerUsers.add(EasemobUser.toRegisterUser(registerUser));
        String result=(String) IM_USER_API.createNewIMUserSingle(registerUsers);
        Gson gson=new Gson();
        EasemobUserResponse response=gson.fromJson(result, EasemobUserResponse.class);
        List<EasemobUser> easemobUsers=response.toEasemobUser();
//        单用户注册，默认应该是第一个
        EasemobUser saveUser=easemobUsers.get(0);
        if (saveUser==null) {
            return Optional.empty();
        }
        saveUser.setPassword(registerUser.getPassword());
        return Optional.of(easemobRepository.save(saveUser));
    }

    @Override
    public JpaRepository<EasemobUser, Integer> getRepository() {
        return easemobRepository;
    }

}
