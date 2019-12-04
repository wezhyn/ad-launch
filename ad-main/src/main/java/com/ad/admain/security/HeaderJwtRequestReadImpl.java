package com.ad.admain.security;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class HeaderJwtRequestReadImpl implements IJwtRequestRead {
    @Override
    public String readJwt(HttpServletRequest httpServletRequest) {
        String token=httpServletRequest.getHeader("X-Token");
        if (token==null || Objects.equals("", token) || Objects.equals(token, "null")) {
            return null;
        }
        return token;
    }
}
