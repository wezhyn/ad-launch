package com.ad.admain.security.repo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 使用 用户的 Id 作为 密钥的关联id，使得插入时变得随机，将可能导致频繁的页分裂操作
 *
 * @author : wezhyn
 * @date : 2019/09/20
 * 用于保存 用户账户和 密钥
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Data
@AllArgsConstructor
public class JwtUserSecret {

    /**
     * 用户 id
     */
    @Id
    private Integer id;
    /**
     * 用户名冗余列，目前无使用
     */
    private String username;

    private String secret;


    /*
        /**********************************************************
        /*  构造器
        /**********************************************************
    */


    public JwtUserSecret() {
    }


}
