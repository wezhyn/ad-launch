package com.ad.admain.security;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * 从 http 请求中加载 jwt 的方法
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface IJwtRequestRead {


    /**
     * request header load jwt field
     *
     * @param httpServletRequest request
     * @return jwt Str
     */
    String readJwt(HttpServletRequest httpServletRequest);
}
