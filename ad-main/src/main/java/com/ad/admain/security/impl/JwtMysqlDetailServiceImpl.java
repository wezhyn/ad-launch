package com.ad.admain.security.impl;

import com.ad.admain.security.jwt.JwtDetailService;
import com.ad.admain.security.repo.JwtUserSecret;
import com.ad.admain.security.repo.JwtUserSecretRepository;
import org.springframework.stereotype.Service;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * 将jwt 密钥保存在数据库中，使用时拿出来
 * id: userId
 * username: username
 * secret: 用户登录时验证密钥
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class JwtMysqlDetailServiceImpl implements JwtDetailService {

    private final JwtUserSecretRepository userSecretRepository;

    public JwtMysqlDetailServiceImpl(JwtUserSecretRepository userSecretRepository) {
        this.userSecretRepository=userSecretRepository;
    }


    @Override
    public String loadSecretById(Integer userId) {
        return userSecretRepository.findById(userId)
                .map(JwtUserSecret::getSecret)
                .orElse("");
    }

    @Override
    public void saveSecretByUsername(Integer id, String username, String secret) {
        userSecretRepository.save(new JwtUserSecret(id, username, secret));
    }

    @Override
    public void deleteSecretByUsername(Integer id) {
        userSecretRepository.deleteById(id);
    }
}
