package com.ad.admain.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AdUserDetailsService extends UserDetailsService {

    /**
     * 拦截的 url
     * @param url /api/user /api/admin
     * @return true
     */
    boolean support(String url);



}
