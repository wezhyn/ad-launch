package com.ad.admain.controller.account.user;

import com.ad.admain.controller.impl.IFileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface GenericUserRepository extends JpaRepository<GenericUser, Integer> {

    /**
     * 通过用户账号获取用户信息
     *
     * @param userName userName
     * @return {@link GenericUser}
     */
    Optional<GenericUser> findByUsername(String userName);

    /**
     * 通过手机获取用户信息
     *
     * @param phone 手机号
     * @return {@link GenericUser}
     */
    Optional<GenericUser> findByMobilePhone(String phone);

    Page<GenericUser> findGenericUsersByEnable(GenericUser.UserEnable able, Pageable pageable);

    /**
     * 修改用户头像
     *
     * @param username 账户
     * @param avatar   {@link IFileUpload#getRelativeName()}
     * @return 1
     */
    @Query("update GenericUser u set u.avatar=:avatar where u.username=:username")
    @Modifying(clearAutomatically = true)
    @Transactional(rollbackFor = Exception.class)
    int updateUserAvatar(@Param("username") String username,
                         @Param("avatar") String avatar);

    /**
     * 修改用户密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 1
     */
    @Query("update GenericUser u set u.password=:password where u.username=:username")
    @Modifying(clearAutomatically = true)
    @Transactional(rollbackFor = Exception.class)
    int updateUserPassword(@Param("username") String username,
                           @Param("password") String password);


    /**
     * 修改用户密码
     *
     * @param id       id
     * @param password 密码
     * @return 1
     */
    @Query("update GenericUser u set u.password=:password where u.id=:id")
    @Modifying(clearAutomatically = true)
    @Transactional(rollbackFor = Exception.class)
    int updateUserPassword(@Param("id") Integer id,
                           @Param("password") String password);
}
