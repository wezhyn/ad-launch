package com.ad.admain.service;

import com.ad.admain.constants.JwtProperties;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AdUserDetailsService extends UserDetailsService {

    /**
     * mark: 为{@link JwtProperties#getLoginInterceptionInclude()}中的key
     *
     * @param mark 拦截标识
     * @return true
     */
    boolean support(String mark);


}
