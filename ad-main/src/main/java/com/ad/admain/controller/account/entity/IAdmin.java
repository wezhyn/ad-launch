package com.ad.admain.controller.account.entity;

import com.ad.admain.controller.account.AuthenticationEnum;
import com.ad.admain.controller.impl.IFileUpload;
import com.ad.admain.utils.RoleAuthenticationUtils;
import com.wezhyn.project.IUserTo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface IAdmin extends IUserTo<Integer> {



    /**
     * 昵称
     *
     * @return nickName
     */
    String getNickName();


    /**
     * id card
     *
     * @return 身份证
     */
    String getIdCard();

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
     * 头像
     *
     * @return {@link IFileUpload#getRelativeName()}
     */
    String getAvatar();

    /**
     * 用户权限
     *
     * @return AuthenticationEnum
     */
    AuthenticationEnum getRoles();

    /**
     * 获取用户的权限
     *
     * @return {@link RoleAuthenticationUtils#forGrantedAuthorities}
     */
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleAuthenticationUtils.forGrantedAuthorities(getRoles());
    }
}
