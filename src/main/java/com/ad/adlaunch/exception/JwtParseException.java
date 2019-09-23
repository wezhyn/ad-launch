package com.ad.adlaunch.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class JwtParseException extends AuthenticationException {
    public JwtParseException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtParseException(String msg) {
        super(msg);
    }
}
