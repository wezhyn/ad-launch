package com.ad.admain.service;

import com.ad.admain.dto.EasemobUser;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface EasemobService extends MessageService, BaseService<EasemobUser, String> {

    /**
     * 向环信注册用户，并保存到数据库
     *
     * @param registerUsers list<User>
     * @return easemobUser
     */
    EasemobUser registerEasemob(EasemobUser registerUsers);

}
