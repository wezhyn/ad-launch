package com.ad.admain.repository;

import com.ad.admain.dto.GenericUser;
import com.ad.admain.to.IFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Repository
public interface GenericUserRepository extends JpaRepository<GenericUser, String> {


    /**
     * 通过用户账号获取用户信息
     * @param userName userName
     * @return {@link GenericUser}
     */
    Optional<GenericUser> findGenericUserByUsername(String userName);


    /**
     * 修改用户头像
     * @param username 账户
     * @param avatar {@link IFileUpload#getRelativeName()}
     * @return 1
     */
    @Query("update GenericUser u set u.avatar=:avatar where u.username=:username")
    @Modifying(clearAutomatically=true)
    @Transactional(rollbackFor=Exception.class)
    int updateUserAvatar(@Param("username") String username,
                         @Param("avatar") String avatar);




}
