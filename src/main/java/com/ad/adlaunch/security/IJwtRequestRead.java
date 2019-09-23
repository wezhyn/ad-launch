package com.ad.adlaunch.security;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface IJwtRequestRead {


    String readJwt(HttpServletRequest httpServletRequest);
}
