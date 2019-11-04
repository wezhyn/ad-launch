package com.ad.admain.controller;

import com.ad.admain.convert.GenericUserMapper;
import com.ad.admain.dto.UserDto;
import com.ad.admain.service.GenericUserService;
import com.ad.admain.to.GenericUser;
import com.ad.admain.to.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RequestMapping("/api/user")
@RestController
public class NoCheckUserController {

    @Autowired
    private GenericUserService genericUserService;
    @Autowired
    private GenericUserMapper genericUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value={"/register", "/create"})
    public ResponseResult register(@RequestBody UserDto userDto) {
        GenericUser requestUser=genericUserMapper.toTo(userDto);
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        Optional<GenericUser> genericUser=genericUserService.save(requestUser);
        return genericUser.map(u->ResponseResult.forSuccessBuilder().withMessage("注册成功").build())
                .orElseGet(()->ResponseResult.forFailureBuilder().withMessage("注册失败").build());
    }
}
