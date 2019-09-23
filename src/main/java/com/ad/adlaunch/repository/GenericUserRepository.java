package com.ad.adlaunch.repository;

import com.ad.adlaunch.dto.GenericUser;
import com.ad.adlaunch.dto.IUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Repository
public interface GenericUserRepository extends JpaRepository<GenericUser, Long> {


    /**
     * 通过用户账号获取用户信息
     * @param userName userName
     * @return {@link GenericUser}
     */
    Optional<GenericUser> findGenericUserByUsername(String userName);

    int deleteGenericUserByUsername(String username);
}
