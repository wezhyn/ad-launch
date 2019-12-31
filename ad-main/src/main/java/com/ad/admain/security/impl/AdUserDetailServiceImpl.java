package com.ad.admain.security.impl;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.account.entity.IUser;
import com.ad.admain.security.AdUserDetails;
import com.ad.admain.security.AdUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service(value="adUserDetailService")
@Slf4j
public class AdUserDetailServiceImpl extends AdUserDetailsService {

    private final static String INTERCEPT_MARK="user";
    private final GenericUserService genericUserService;

    public AdUserDetailServiceImpl(GenericUserService genericUserService) {
        this.genericUserService=genericUserService;
    }

    private AdUser createUser(IUser user) {
        if (log.isDebugEnabled()) {
            log.debug("找到用户 : " + user);
        }
        return new AdUser(user.getId(), user.getUsername(), user.getPassword(), !user.isLock(), user.getAuthorities());
    }

    @Override
    public boolean support(String mark) {
        return INTERCEPT_MARK.equalsIgnoreCase(mark);
    }

    @Override
    protected AdUserDetails getUserDetails(String username) {
//        IUser user=genericUserService.getById(username)
        IUser user=genericUserService.getUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("无法找到该用户 : " + username));
        if (log.isDebugEnabled()) {
            log.debug("找到用户 : " + user);
        }
        return createUser(user);
    }

}
