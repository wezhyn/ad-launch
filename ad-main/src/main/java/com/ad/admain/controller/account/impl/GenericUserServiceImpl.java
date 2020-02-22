package com.ad.admain.controller.account.impl;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.account.dto.UserDto;
import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.controller.account.repository.GenericUserRepository;
import com.ad.admain.security.jwt.JwtDetailService;
import com.wezhyn.project.AbstractBaseService;
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
    public GenericUser save(GenericUser object) {
        return super.save(object);
    }

    @Override
    public GenericUserRepository getRepository() {
        return this.genericUserRepository;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int modifyUserPasswordById(Integer id, String username, String password) {
        String newPasword=passwordEncoder.encode(password);
        String secret=com.wezhyn.project.utils.StringUtils.getRandomString(50);
//        todo:解耦
        jwtDetailService.saveSecretByUsername(id, username, secret);
        return genericUserRepository.updateUserPassword(id, newPasword);
    }

    @Override
    public Optional<GenericUser> getUserByUsername(String username) {
        return getRepository().findByUsername(username);
    }

    @Override
    public Optional<String> getUserAvatar(String username) {
        if (StringUtils.isEmpty(username)) {
            return Optional.empty();
        }
        Optional<GenericUser> user=getRepository().findByUsername(username);
        return user.map(GenericUser::getAvatar);
    }

    @Override
    public int updateUserAvatar(String username, String avatarKey) {
        return getRepository().updateUserAvatar(username, avatarKey);
    }


    @Override
    public Optional<GenericUser> updateUserAuthenticationInfo(UserDto userDto) {
        final Optional<GenericUser> savedUser=getRepository().findById(userDto.getId());
        return Optional.of(savedUser.map(u->{
            if (u.getEnable().getEnableNum()==0) {
                u.setRealName(userDto.getRealname());
                u.setIdCard(userDto.getIdCard());
                return save(u);
            }
            return null;
        }).get());
    }

    @Override
    public Optional<GenericUser> getOneByUsernameOrPhone(String s) {
        if (Character.isDigit(s.charAt(0))) {
            return getRepository().findByMobilePhone(s);
        }
        return getRepository().findByUsername(s);
    }

    @Override
    public int modifyUserAvatar(String username, String avatar) {
        return genericUserRepository.updateUserAvatar(username, avatar);
    }
}
