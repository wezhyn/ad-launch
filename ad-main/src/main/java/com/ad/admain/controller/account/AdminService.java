package com.ad.admain.controller.account;

import com.ad.admain.controller.account.entity.Admin;
import com.wezhyn.project.BaseService;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AdminService extends BaseService<Admin, Integer>, CommonAccountService<Admin, Integer> {

    Optional<Admin> getByUsername(String s);
}
