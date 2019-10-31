package com.ad.admain.service.impl;

import com.ad.admain.service.AdUserDetailsService;
import com.ad.admain.service.GenericUserService;
import com.ad.admain.to.IUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service(value="adUserDetailService")
@Slf4j
public class AdUserDetailServiceImpl implements AdUserDetailsService {

    private final GenericUserService genericUserService;
    private final static String INTERCEPT_MARK="user";

    public AdUserDetailServiceImpl(GenericUserService genericUserService) {
        this.genericUserService=genericUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("无效的账户： " + username + "，请检查是否为空");
        }
//        IUser user=genericUserService.getById(username)
        IUser user=genericUserService.getUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("无法找到该用户 : " + username));

        if (log.isDebugEnabled()) {
            log.debug("找到用户 : " + user);
        }
        return createUser(user);
    }

    private User createUser(IUser user) {
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public boolean support(String mark) {
        return INTERCEPT_MARK.equalsIgnoreCase(mark);
    }

}
