package com.ad.admain.controller.account;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.account.dto.UserDto;
import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.GenericUserMapper;
import com.wezhyn.project.BaseService;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RequestMapping("/api/user")
@RestController
public class NoCheckUserController extends AbstractBaseController<UserDto, Integer, GenericUser> {

    private final GenericUserService genericUserService;
    private final GenericUserMapper genericUserMapper;
    private final PasswordEncoder passwordEncoder;

    public NoCheckUserController(GenericUserService genericUserService, GenericUserMapper genericUserMapper, PasswordEncoder passwordEncoder) {
        this.genericUserService=genericUserService;
        this.genericUserMapper=genericUserMapper;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping(value={"/register", "/create"})
    public ResponseResult register(@RequestBody UserDto userDto) {
        return createTo(userDto);
    }

    @Override
    protected GenericUser preSave(GenericUser to) {
        to.setPassword(passwordEncoder.encode(to.getPassword()));
        return super.preSave(to);
    }

    @Override
    public BaseService<GenericUser, Integer> getService() {
        return genericUserService;
    }

    @Override
    public AbstractMapper<GenericUser, UserDto> getConvertMapper() {
        return genericUserMapper;
    }
}
