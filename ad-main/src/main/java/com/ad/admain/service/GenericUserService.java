package com.ad.admain.service;

import com.ad.admain.dto.GenericUser;
import com.ad.admain.to.IFileUpload;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface GenericUserService extends BaseService<GenericUser,String> {


    /**
     * 修改用户头像
     * @param username 主键
     * @param avatar 头像地址 {@link IFileUpload#getRelativeName()}
     * @return 1
     */
    int modifyUserAvatar(String username,String avatar);
}
