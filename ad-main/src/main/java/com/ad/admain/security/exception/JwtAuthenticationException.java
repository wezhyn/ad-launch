package com.ad.admain.security.exception;

/**
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
