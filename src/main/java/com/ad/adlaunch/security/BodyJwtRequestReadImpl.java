package com.ad.adlaunch.security;

import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class BodyJwtRequestReadImpl implements IJwtRequestRead {
    @Override
    public String readJwt(HttpServletRequest httpServletRequest) {
        String jwt=httpServletRequest.getParameter("token");
        return jwt;
    }
}
