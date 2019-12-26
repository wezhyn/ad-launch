package com.ad.admain.security.exception;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class AdUsernamePasswordException extends JwtAuthenticationException {


    public AdUsernamePasswordException(String msg, Throwable t) {
        super(msg, t);
    }

    public AdUsernamePasswordException(String msg) {
        super(msg);
    }
}
