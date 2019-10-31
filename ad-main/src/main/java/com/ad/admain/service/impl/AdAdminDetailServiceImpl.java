package com.ad.admain.service.impl;

import com.ad.admain.service.AdUserDetailsService;
import com.ad.admain.service.AdminService;
import com.ad.admain.to.Admin;
import com.ad.admain.to.IAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class AdAdminDetailServiceImpl implements AdUserDetailsService {
    private final static String INTERCEPT_MARK="admin";
    private final AdminService adminService;


    public AdAdminDetailServiceImpl(AdminService adminService) {
        this.adminService=adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("无效的账户： " + username + "，请检查是否为空");
        }
        Optional<Admin> user=adminService.getByUsername(username);
        return user.map(this::createUser)
                .orElseThrow(()->new UsernameNotFoundException("无法找到该用户 : " + username));
    }

    private User createUser(IAdmin user) {
        if (log.isDebugEnabled()) {
            log.debug("找到用户 : " + user);
        }
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public boolean support(String mark) {
        return INTERCEPT_MARK.equalsIgnoreCase(mark);
    }
}
