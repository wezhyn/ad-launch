package com.ad.admain.controller.account;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

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
            String field=bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("|"));
            return ResponseResult.forFailureBuilder()
                    .withMessage(field).build();
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
