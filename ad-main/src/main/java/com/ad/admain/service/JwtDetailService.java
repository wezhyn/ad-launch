package com.ad.admain.service;

import com.ad.admain.to.IUser;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface JwtDetailService {

    /**
     * 通过 用户账号 加载所在的 jwt 加密密钥
     * @param username {@link IUser#getUsername()}
     * @return secret
     */
    String loadSecretByUsername(String username);


    void saveSecretByUsername(String username, String secret);

    void deleteSecretByUsername(String username);
}
