package com.ad.admain.security.exception;

/**
 * 当此异常默认由 {@link com.ad.admain.security.filter.JwtAuthenticationFailFilter} 拦截，
 * 若有其他异常拦截器拦截，请返回状态码 50008
 *
 * @author wezhyn
 * @since 12.26.2019
 */
public class JwtAuthenticationException extends RuntimeException {


    public JwtAuthenticationException() {
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtAuthenticationException(Throwable cause) {
        super(cause);
    }
}
