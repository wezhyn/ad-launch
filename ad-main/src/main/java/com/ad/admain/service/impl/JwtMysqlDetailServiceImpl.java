package com.ad.admain.service.impl;

import com.ad.admain.dto.JwtUserSecret;
import com.ad.admain.repository.JwtUserSecretRepository;
import com.ad.admain.service.JwtDetailService;
import org.springframework.stereotype.Service;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 *     将jwt 密钥保存在数据库中，使用时拿出来
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class JwtMysqlDetailServiceImpl implements JwtDetailService{

    private final JwtUserSecretRepository userSecretRepository;

    public JwtMysqlDetailServiceImpl(JwtUserSecretRepository userSecretRepository) {
        this.userSecretRepository=userSecretRepository;
    }


    @Override
    public String loadSecretByUsername(String username) {
        JwtUserSecret userSecret=userSecretRepository.findById(username)
                .orElse(JwtUserSecret.EMPTY_USER_SECRET);
        return userSecret.getSecret();
    }

    @Override
    public void saveSecretByUsername(String username, String secret) {
        userSecretRepository.save(new JwtUserSecret(username,secret));
    }

    @Override
    public void deleteSecretByUsername(String username) {
        userSecretRepository.deleteById(username);
    }
}
