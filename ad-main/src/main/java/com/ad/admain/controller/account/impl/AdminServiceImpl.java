package com.ad.admain.controller.account.impl;

import com.ad.admain.controller.account.AdminService;
import com.ad.admain.controller.account.entity.Admin;
import com.ad.admain.controller.account.repository.AdminRepository;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class AdminServiceImpl extends AbstractBaseService<Admin, Integer> implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository=adminRepository;
    }

    @Override
    public AdminRepository getRepository() {
        return adminRepository;
    }

    @Override
    public Optional<Admin> getByUsername(String s) {
        return getRepository().findByUsername(s);
    }

    @Override
    public Optional<Admin> getOneByUsernameOrPhone(String s) {
        if (Character.isDigit(s.charAt(0))) {
            return getRepository().findByMobilePhone(s);
        }
        return getRepository().findByUsername(s);
    }

    @Override
    public Optional<String> getUserAvatar(String username) {
//    todo:modify
        if (StringUtils.isEmpty(username)) {
            return Optional.empty();
        }
        Optional<Admin> user=getRepository().findByUsername(username);
        return user.map(Admin::getAvatar);
    }

    @Override
    public int modifyUserAvatar(String username, String avatar) {
        return getRepository().updateUserAvatar(username, avatar);
    }
}
