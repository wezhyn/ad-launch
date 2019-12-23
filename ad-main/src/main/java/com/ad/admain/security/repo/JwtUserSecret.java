package com.ad.admain.security.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * 用于保存 用户账户和 密钥
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserSecret {

    @Id
    private Integer id;
    private String username;

    private String secret;

}
