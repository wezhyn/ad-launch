package com.ad.adlaunch.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface IUser  {

    /**
     * 获取用户账号
     *
     * @return userName
     */
    String getUsername();

    /**
     * 获取用户密码
     *
     * @return password
     */
    String getPassword();

    /**
     * 获取用户邮箱
     *
     * @return email
     */
    String getEmail();

    /**
     * 获取用户的权限，从小到大 普通用户返回： User ;管理员返回：user Admin
     * @return
     */
    Collection<? extends GrantedAuthority> getAuthorities();
}
