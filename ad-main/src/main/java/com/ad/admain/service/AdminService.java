package com.ad.admain.service;

import com.ad.admain.to.Admin;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AdminService extends BaseService<Admin, Integer> {

    Optional<Admin> getByUsername(String s);
}
