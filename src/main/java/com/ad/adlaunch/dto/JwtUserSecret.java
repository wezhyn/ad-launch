package com.ad.adlaunch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 *     用于保存 用户账户和 密钥
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserSecret {

    public final static JwtUserSecret EMPTY_USER_SECRET=new JwtUserSecret();

    @Id
    private String username;

    private String secret;

}
