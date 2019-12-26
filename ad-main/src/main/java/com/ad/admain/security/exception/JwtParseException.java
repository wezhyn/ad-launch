package com.ad.admain.security.exception;

/**
 * JWT 验证失败异常，返回状态码 50008
 *
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class JwtParseException extends JwtAuthenticationException {
    public JwtParseException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtParseException(String msg) {
        super(msg);
    }
}
