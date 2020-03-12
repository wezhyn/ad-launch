package com.ad.admain.controller.account;

import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.controller.account.user.UserDto;
import com.ad.admain.controller.impl.IFileUpload;
import com.wezhyn.project.BaseService;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface GenericUserService extends BaseService<GenericUser, Integer>, CommonAccountService<GenericUser, Integer> {


    int modifyUserPasswordById(Integer id, String username, String password);


    Optional<GenericUser> getUserByUsername(String username);

    /**
     * 更新用户头像
     *
     * @param username  username
     * @param avatarKey 头像地址 {@link IFileUpload#getRelativeName()}
     * @return int
     */
    int updateUserAvatar(String username, String avatarKey);


    /**
     * 仅当 {@link GenericUser#getEnable()}  == {@code NOT_AUTHENTICATION} 时，才更新实名信息
     *
     * @param userDto userDto
     * @return user
     */
    Optional<GenericUser> updateUserAuthenticationInfo(UserDto userDto);

}
