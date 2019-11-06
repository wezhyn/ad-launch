package com.ad.admain.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author wezhyn
 * @date 2019/11/06
 * <p>
 * 额外包含用户的自然主键
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AdUserDetails extends UserDetails {

    /**
     * 用户 自增主键
     *
     * @return id
     */
    Integer getId();

}
