package com.ad.admain.dto;

import com.ad.admain.common.IBaseTo;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.to.IFileUpload;
import com.ad.admain.utils.RoleAuthenticationUtils;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface IUser extends IBaseTo<String > {

    /**
     * 获取用户账号
     *
     * @return userName
     */
    String getUsername();

    /**
     * 昵称
     *
     * @return nickName
     */
    String getNickName();

    /**
     * 获取真实名字：身份认证过的名字
     *
     * @return realName
     */
    String getRealName();

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
     * 获取性别
     *
     * @return sex
     */
    String getSex();

    /**
     * 生日
     *
     * @return localDate
     */
    LocalDate getBirthDay();

    /**
     * 手机号
     *
     * @return mobile
     */
    String getMobilePhone();

    /**
     * 用户权限
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
