package com.ad.adlaunch.security;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class HeaderJwtRequestReadImpl implements IJwtRequestRead {
    @Override
    public String readJwt(HttpServletRequest httpServletRequest) {
        String jwt=httpServletRequest.getHeader("X-Token");
        return jwt;
    }
}
