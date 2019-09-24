package com.ad.adlaunch.service.impl;

import com.ad.adlaunch.dto.Admin;
import com.ad.adlaunch.repository.AdminRepository;
import com.ad.adlaunch.service.AbstractBaseService;
import com.ad.adlaunch.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class AdminServiceImpl extends AbstractBaseService<Admin,String >implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public JpaRepository<Admin, String> getRepository() {
        return adminRepository;
    }

    @Override
    public Admin getEmpty() {
        return Admin.EMPTY_ADMIN;
    }
}
