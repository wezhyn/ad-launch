package com.ad.admain.controller.account;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.controller.account.user.UserDto;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.GenericUserMapper;
import com.wezhyn.project.BaseService;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseResult register(@Valid @RequestBody RegisterDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String field = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("|"));
            return ResponseResult.forFailureBuilder()
                .withMessage(field).build();
        }
        final Optional<GenericUser> hasMobile = genericUserService.getOneByUsernameOrPhone(
            userDto.getMobilePhone());
        if (hasMobile.isPresent()) {
            return ResponseResult.forFailureBuilder().withMessage("该手机账户已被绑定").build();
        }
        if (genericUserService.getUserByUsername(userDto.getUsername()).isPresent()) {
            return ResponseResult.forFailureBuilder().withMessage("该用户名已经被占用").build();
        }
        return createTo(userDto);
    }

    @ExceptionHandler(value=Exception.class)
    public ResponseResult error(Exception e) {
        return ResponseResult.forFailureBuilder()
                .withMessage(e.getMessage())
                .build();
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
