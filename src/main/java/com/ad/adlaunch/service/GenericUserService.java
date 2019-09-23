package com.ad.adlaunch.service;

import com.ad.adlaunch.dto.GenericUser;
import com.ad.adlaunch.dto.IUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface GenericUserService {

    /**
     * 通过账号获取用户信息
     * @param userName userName
     * @return IUser
     */
    IUser getUserByUserName(String userName);

    GenericUser saveGenericUser(GenericUser user);

    Page<GenericUser> getGenericList(Pageable pageable);


    GenericUser updateGenericUser(GenericUser user);

    int deleteGenericUser(String username);

    int modifyUserAvatar(String username,String avatar);
}
