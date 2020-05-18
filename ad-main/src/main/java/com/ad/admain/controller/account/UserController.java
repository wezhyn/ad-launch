package com.ad.admain.controller.account;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.controller.account.user.UserDto;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.GenericUserMapper;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.controller.NoNestResponseResult;
import com.wezhyn.project.controller.ResponseResult;
import com.wezhyn.project.exception.UpdateOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
public class UserController extends AbstractBaseController<UserDto, Integer, GenericUser> {


    private final GenericUserService genericUserService;
    private final GenericUserMapper genericUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserController(GenericUserService genericUserService, GenericUserMapper genericUserMapper) {
        this.genericUserService = genericUserService;
        this.genericUserMapper = genericUserMapper;
    }

    @GetMapping("/info")
    public NoNestResponseResult<UserDto> info(@AuthenticationPrincipal AdAuthentication authentication) {
        String name = authentication.getName();
        Optional<GenericUser> user = genericUserService.getUserByUsername(name);
        return user.map(u -> NoNestResponseResult.successResponseResult("", genericUserMapper.toDto(u)))
                .orElse(NoNestResponseResult.failureResponseResult("获取用户信息失败"));
    }


    @PostMapping("/password")
    public ResponseResult editPassword(
            @AuthenticationPrincipal AdAuthentication authentication,
            @RequestBody PasswordModifyWrap passwordModifyWrap) {
//        默认结果为失败 result==-1
        Integer id = authentication.getId();
        String newPwd = passwordModifyWrap.getNewPwd();
        String oldPwd = passwordModifyWrap.getOldPwd();
        int result = -1;
        GenericUser genericUser = genericUserService.getById(id)
//        GenericUser genericUser=genericUserService.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("无该用户信息"));
        if (!passwordEncoder.matches(oldPwd, genericUser.getPassword())) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("旧密码错误")
                    .withCode(50000)
                    .build();
        }
        try {
            result = genericUserService.modifyUserPasswordById(id, authentication.getName(), newPwd);
            return ResponseResult.forSuccessBuilder()
                    .withMessage("修改密码成功")
                    .withCode(20000)
                    .build();
        } catch (Exception e) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("密码修改失败")
                    .withCode(50000).build();
        }
    }

    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name = "limit", defaultValue = "10") int limit, @RequestParam(name = "page", defaultValue = "1") int page) {
        return listDto(limit, page);
    }

    @PostMapping("/update")
    public ResponseResult editUser(@RequestBody UserDto userDto) {
        userDto.setIdCard(null);
        userDto.setRealname(null);
        userDto.setPassword(null);
        userDto.setStatus(null);
        return update(userDto);
    }


    @Override
    protected GenericUser preUpdate(GenericUser to) {
        final AdAuthentication authentication = (AdAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UpdateOperationException("当前用户未认证");
        }
        to.setId(authentication.getId());
        return to;
    }

    @Override
    public GenericUserService getService() {
        return genericUserService;
    }

    @ExceptionHandler
    public ResponseResult handleError(Exception e) {
        String m = e.getMessage();
        return ResponseResult.forFailureBuilder()
                .withMessage(m == null ? m : "操作失败")
                .build();
    }

    @Override
    public AbstractMapper<GenericUser, UserDto> getConvertMapper() {
        return genericUserMapper;
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody UserDto userDto) {
        return delete(userDto);
    }
}
