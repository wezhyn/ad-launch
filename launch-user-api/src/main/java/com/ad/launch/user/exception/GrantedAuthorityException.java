package com.ad.launch.user.exception;

/**
 * 创建对应用户权限失败
 *
 * @author wezhyn
 * @since 03.12.2020
 */
public class GrantedAuthorityException extends RuntimeException {

    public GrantedAuthorityException() {
    }

    public GrantedAuthorityException(String message) {
        super(message);
    }
}
