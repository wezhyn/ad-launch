package com.ad.admain.service;

import com.ad.admain.dto.IFileUpload;
import com.ad.admain.to.GenericUser;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface GenericUserService extends BaseService<GenericUser, Integer> {


    /**
     * 修改用户头像
     *
     * @param username 主键
     * @param avatar   头像地址 {@link IFileUpload#getRelativeName()}
     * @return 1
     */
    int modifyUserAvatar(String username, String avatar);

    int modifyUserPassword(String username, String password);

    int modifyUserPasswordById(Integer id, String password);

    /**
     * 获取用户头像
     *
     * @param username username
     * @return username：null | “” 返回Optional.empty()
     */
    Optional<String> getUserAvatar(String username);

    Optional<GenericUser> getUserByUsername(String username);

    /**
     * 更新用户头像
     *
     * @param username  username
     * @param avatarKey 头像地址 {@link IFileUpload#getRelativeName()}
     * @return int
     */
    int updateUserAvatar(String username, String avatarKey);

}
