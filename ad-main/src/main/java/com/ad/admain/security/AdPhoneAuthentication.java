package com.ad.admain.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 根据 code：
 * 有 code 进行实际登录
 * 无 code 触发短信发送
 *
 * @author wezhyn
 * @since 02.22.2020
 */
public class AdPhoneAuthentication extends AdAuthentication {

    public AdPhoneAuthentication(String phone, String code, String mark) {
        super(phone, code, mark);
    }

    public AdPhoneAuthentication(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(principal, "", authorities);
    }
}
