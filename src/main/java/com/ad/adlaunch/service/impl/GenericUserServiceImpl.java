package com.ad.adlaunch.service.impl;

import com.ad.adlaunch.dto.GenericUser;
import com.ad.adlaunch.repository.GenericUserRepository;
import com.ad.adlaunch.service.AbstractBaseService;
import com.ad.adlaunch.service.GenericUserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * 当查找不到时不返回 Null ,默认返回 Empty_User
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class GenericUserServiceImpl extends AbstractBaseService<GenericUser, String> implements GenericUserService {

    private final GenericUserRepository genericUserRepository;


    public GenericUserServiceImpl(GenericUserRepository genericUserRepository) {
        this.genericUserRepository=genericUserRepository;
    }

    /*
        /**********************************************************
        /* 成员方法
        /**********************************************************
    */


    @Override
    public int modifyUserAvatar(String username, String avatar) {
        return genericUserRepository.updateUserAvatar(username, avatar);
    }



    @Override
    public JpaRepository<GenericUser, String> getRepository() {
        return this.genericUserRepository;
    }

    @Override
    public GenericUser getEmpty() {
        return GenericUser.EMPTY_USER;
    }
}
