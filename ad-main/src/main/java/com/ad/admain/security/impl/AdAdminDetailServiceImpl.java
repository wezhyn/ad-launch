package com.ad.admain.security.impl;

import com.ad.admain.controller.account.AdminService;
import com.ad.admain.controller.account.entity.Admin;
import com.ad.admain.controller.account.entity.IAdmin;
import com.ad.admain.security.AdUserDetails;
import com.ad.admain.security.AdUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * 实现类
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
@Service
public class AdAdminDetailServiceImpl extends AdUserDetailsService {
    private final static String INTERCEPT_MARK="admin";
    private final AdminService adminService;


    public AdAdminDetailServiceImpl(AdminService adminService) {
        this.adminService=adminService;
    }

    private AdUser createUser(IAdmin user) {
        if (log.isDebugEnabled()) {
            log.debug("找到用户 : " + user);
        }
        return new AdUser(user.getId(), user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean support(String mark) {
        return INTERCEPT_MARK.equalsIgnoreCase(mark);
    }

    @Override
    public AdUserDetails getUserDetails(String username) {
        Optional<Admin> user=adminService.getByUsername(username);
        return user.map(this::createUser)
                .orElseThrow(()->new UsernameNotFoundException("无法找到该用户 : " + username));
    }
}
