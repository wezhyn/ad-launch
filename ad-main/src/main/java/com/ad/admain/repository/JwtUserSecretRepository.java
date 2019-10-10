package com.ad.admain.repository;

import com.ad.admain.to.JwtUserSecret;
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
