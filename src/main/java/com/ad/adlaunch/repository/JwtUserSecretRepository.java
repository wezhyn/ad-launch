package com.ad.adlaunch.repository;

import com.ad.adlaunch.dto.JwtUserSecret;
import com.ad.adlaunch.service.impl.JwtMysqlDetailServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Repository
public interface JwtUserSecretRepository extends JpaRepository<JwtUserSecret,String> {


}
