package com.ad.admain.service.impl;

import com.ad.admain.dto.Admin;
import com.ad.admain.repository.AdminRepository;
import com.ad.admain.service.AbstractBaseService;
import com.ad.admain.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository=adminRepository;
    }

    @Override
    public JpaRepository<Admin, String> getRepository() {
        return adminRepository;
    }

    @Override
    public Admin getEmpty() {
        return Admin.EMPTY_ADMIN;
    }

}
