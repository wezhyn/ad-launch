package com.ad.admain.service.impl;

import com.ad.admain.dto.GenericUser;
import com.ad.admain.repository.GenericUserRepository;
import com.ad.admain.service.AbstractBaseService;
import com.ad.admain.service.GenericUserService;
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

    @Override
    public GenericUser update(GenericUser newObject) {

        return newObject;
    }
}
