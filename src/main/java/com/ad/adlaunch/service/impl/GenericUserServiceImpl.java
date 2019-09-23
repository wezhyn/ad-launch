package com.ad.adlaunch.service.impl;

import com.ad.adlaunch.dto.GenericUser;
import com.ad.adlaunch.dto.IUser;
import com.ad.adlaunch.repository.GenericUserRepository;
import com.ad.adlaunch.service.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class GenericUserServiceImpl implements GenericUserService {

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
    public IUser getUserByUserName(String userName) {
        return genericUserRepository.findGenericUserByUsername(userName)
                .orElse(GenericUser.EMPTY_USER);
    }

    @Override
    public GenericUser saveGenericUser(GenericUser user) {
        return genericUserRepository.save(user);
    }


    @Override
    public Page<GenericUser> getGenericList(Pageable pageable) {

        return genericUserRepository.findAll(pageable);
    }

    @Override
    public GenericUser updateGenericUser(GenericUser user) {
       return genericUserRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int deleteGenericUser(String username) {
        if (StringUtils.isEmpty(username)) {
            return 0;
        }
        return genericUserRepository.deleteGenericUserByUsername(username);
    }

    @Override
    public int modifyUserAvatar(String username, String avatar) {
       return genericUserRepository.updateUserAvatar(username, avatar);
    }
}
