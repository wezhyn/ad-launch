package com.ad.adlaunch.service.impl;

import com.ad.adlaunch.dto.GenericUser;
import com.ad.adlaunch.exception.UserOperateException;
import com.ad.adlaunch.repository.GenericUserRepository;
import com.ad.adlaunch.service.GenericUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * 当查找不到时不返回 Null ,默认返回 Empty_User
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
    public GenericUser getById(String userName) {
        return genericUserRepository.findById(userName)
                .orElse(GenericUser.EMPTY_USER);
    }

    @Override
    public GenericUser save(GenericUser user) {
        return genericUserRepository.save(user);
    }


    @Override
    public Page<GenericUser> getList(Pageable pageable) {
        return genericUserRepository.findAll(pageable);
    }

    @Override
    public GenericUser update(GenericUser user) {
        return genericUserRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor=UserOperateException.class)
    public void delete(String username) {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        genericUserRepository.deleteById(username);
    }

    @Override
    public int modifyUserAvatar(String username, String avatar) {
        return genericUserRepository.updateUserAvatar(username, avatar);
    }
}
