package com.ad.admain.security.jwt;


import com.ad.launch.user.IUser;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * id: {@link IUser#getId()}
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface JwtDetailService {

    /**
     * 通过 用户账号 加载所在的 jwt 加密密钥
     *
     * @param userId {@link IUser#getId()}
     * @return if not exist return ""
     */
    String loadSecretById(Integer userId);


    void saveSecretByUsername(Integer id, String username, String secret);

    void deleteSecretByUsername(Integer id);
}
