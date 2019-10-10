package com.ad.admain.repository;

import com.ad.admain.to.EasemobUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Repository
public interface EasemobRepository extends JpaRepository<EasemobUser, String> {

}
