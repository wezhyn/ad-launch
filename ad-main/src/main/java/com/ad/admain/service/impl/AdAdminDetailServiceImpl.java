package com.ad.admain.service.impl;

import com.ad.admain.dto.Admin;
import com.ad.admain.dto.IAdmin;
import com.ad.admain.service.AdUserDetailsService;
import com.ad.admain.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
@Service
public class AdAdminDetailServiceImpl implements AdUserDetailsService {
    private final AdminService adminService;

    public AdAdminDetailServiceImpl(AdminService adminService) {
        this.adminService=adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("无效的账户： " + username + "，请检查是否为空");
        }
        IAdmin user=adminService.getById(username);
        if (user==Admin.EMPTY_ADMIN) {
            throw new UsernameNotFoundException("无法找到该用户 : " + username);
        }
        if (log.isDebugEnabled()) {
            log.debug("找到用户 : " + user);
        }
        return createUser(user);
    }

    private User createUser(IAdmin user) {
        return new User(user.getId(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public boolean support(String url) {
        return url!=null && url.startsWith("/api/admin");
    }
}
