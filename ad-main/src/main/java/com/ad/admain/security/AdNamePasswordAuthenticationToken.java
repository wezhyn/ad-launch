package com.ad.admain.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Getter
public class AdNamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {


    private String loginUrl;
    private Integer id;

/*
    /**********************************************************
    /*  构造器
    /**********************************************************
*/

    public AdNamePasswordAuthenticationToken(String userName, String password, String url) {
        super(userName, password);
        this.loginUrl=url;
    }

    public AdNamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getUserName() {
        return (String) getPrincipal();
    }

    public String getPassword() {
        return (String) getCredentials();
    }

}
