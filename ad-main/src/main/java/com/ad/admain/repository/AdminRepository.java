package com.ad.admain.repository;

import com.ad.admain.dto.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AdminRepository extends JpaRepository<Admin, String> {

}
