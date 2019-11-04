package com.ad.admain.service.impl;

import com.ad.admain.repository.GenericUserRepository;
import com.ad.admain.service.AbstractBaseService;
import com.ad.admain.service.GenericUserService;
import com.ad.admain.service.JwtDetailService;
import com.ad.admain.to.GenericUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;


/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * 当查找不到时不返回 Null ,默认返回 Empty_User
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class GenericUserServiceImpl extends AbstractBaseService<GenericUser, Integer> implements GenericUserService {

    private final GenericUserRepository genericUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtDetailService jwtDetailService;

    @Autowired

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
    public Optional<GenericUser> save(GenericUser object) {
        return super.save(object);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int modifyUserPassword(String username, String password) {
        String newPasword=passwordEncoder.encode(password);
        String secret=com.ad.admain.utils.StringUtils.getRandomString(50);
        jwtDetailService.saveSecretByUsername(username, secret);
        return genericUserRepository.updateUserPassword(username, newPasword);
    }

    @Override
    public int modifyUserPasswordById(Integer id, String password) {
        String newPasword=passwordEncoder.encode(password);
        return genericUserRepository.updateUserPassword(id, newPasword);
    }

    @Override
    public GenericUserRepository getRepository() {
        return this.genericUserRepository;
    }


    @Override
    public int updateUserAvatar(String username, String avatarKey) {
        return getRepository().updateUserAvatar(username, avatarKey);
    }

    @Override
    public Optional<GenericUser> getUserByUsername(String username) {
        return getRepository().findGenericUserByUsername(username);
    }

    @Override
    public Optional<String> getUserAvatar(String username) {
        if (StringUtils.isEmpty(username)) {
            return Optional.empty();
        }
        Optional<GenericUser> user=getRepository().findGenericUserByUsername(username);
        return user.map(GenericUser::getAvatar);
    }
}
