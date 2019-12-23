package com.ad.admain.controller.account.repository;

import com.ad.admain.controller.account.entity.Admin;
import com.ad.admain.controller.impl.IFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findByUsername(String username);

    /**
     * 修改用户头像
     *
     * @param username 账户
     * @param avatar   {@link IFileUpload#getRelativeName()}
     * @return 1
     */
    @Query("update Admin u set u.avatar=:avatar where u.username=:username")
    @Modifying(clearAutomatically=true)
    @Transactional(rollbackFor=Exception.class)
    int updateUserAvatar(@Param("username") String username,
                         @Param("avatar") String avatar);
}
