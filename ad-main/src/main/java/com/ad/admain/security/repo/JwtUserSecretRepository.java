package com.ad.admain.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * jwt 密钥的加载
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Repository
public interface JwtUserSecretRepository extends JpaRepository<JwtUserSecret, Integer> {


}
