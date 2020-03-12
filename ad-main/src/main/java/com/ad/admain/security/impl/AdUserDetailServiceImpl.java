package com.ad.admain.security.impl;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.security.AdUserDetails;
import com.ad.admain.security.AdUserDetailsService;
import com.ad.launch.user.IUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<SimpleGrantedAuthority> authorities=user.getAuthorities()
                .stream()
                .map(x->new SimpleGrantedAuthority(x.getAuthority()))
                .collect(Collectors.toList());
        return new AdUser(user.getId(), user.getUsername(), user.getPassword(), user.isLock(), authorities);
    }

    @Override
    public boolean support(String mark) {
        return INTERCEPT_MARK.equalsIgnoreCase(mark);
    }

    @Override
    protected AdUserDetails getUserDetails(String username) {
//        IUser user=genericUserService.getById(username)
        IUser user=genericUserService.getOneByUsernameOrPhone(username)
                .orElseThrow(()->new UsernameNotFoundException("无法找到该用户 : " + username));
        if (log.isDebugEnabled()) {
            log.debug("找到用户 : " + user);
        }
        return createUser(user);
    }

}
