package com.ad.admain.message;

import com.wezhyn.project.BaseService;

import java.util.Optional;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface EasemobService extends MessageService, BaseService<EasemobUser, Integer> {

    /**
     * 向环信注册用户，并保存到数据库
     *
     * @param registerUsers list<User>
     * @return easemobUser
     */
    Optional<EasemobUser> registerEasemob(EasemobUser registerUsers);

}
