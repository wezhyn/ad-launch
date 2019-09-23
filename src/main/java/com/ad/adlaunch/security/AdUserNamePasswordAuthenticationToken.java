package com.ad.adlaunch.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class AdUserNamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    

/*
    /**********************************************************
    /*  构造器
    /**********************************************************
*/

    public AdUserNamePasswordAuthenticationToken(String userName,String password) {
        super(userName,password);
    }

    public AdUserNamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getUserName() {
        return (String) getPrincipal();
    }

    public String getPassword() {
        return (String) getCredentials();
    }

}
