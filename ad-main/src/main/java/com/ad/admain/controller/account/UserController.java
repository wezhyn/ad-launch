package com.ad.admain.controller.account;

import com.ad.admain.controller.SimpleResponseResult;
import com.ad.admain.convert.GenericUserMapper;
import com.ad.admain.dto.UserDto;
import com.ad.admain.security.AdAuthentication;
import com.ad.admain.to.GenericUser;
import com.ad.admain.to.PasswordModifyWrap;
import com.ad.admain.to.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class UserController {


    private final GenericUserService genericUserService;
    private final GenericUserMapper genericUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserController(GenericUserService genericUserService, GenericUserMapper genericUserMapper) {
        this.genericUserService=genericUserService;
        this.genericUserMapper=genericUserMapper;
    }

    @GetMapping("/info")
    public SimpleResponseResult<UserDto> info(@AuthenticationPrincipal AdAuthentication authentication) {
        String name=authentication.getName();
//        Optional<GenericUser> user=genericUserService.getById(name);
        Optional<GenericUser> user=genericUserService.getUserByUsername(name);
        return user.map(u->SimpleResponseResult.successResponseResult("", genericUserMapper.toDto(u)))
                .orElse(SimpleResponseResult.failureResponseResult("获取用户信息失败"));
    }

    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<GenericUser> genericUsers=genericUserService.getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", genericUserMapper.toDtoList(genericUsers.getContent()))
                .withData("total", genericUsers.getTotalElements())
                .build();
    }

    @PostMapping("/password")
    public ResponseResult editPassword(
            @AuthenticationPrincipal Authentication authentication,
            @RequestBody PasswordModifyWrap passwordModifyWrap) {
//        默认结果为失败 result==-1
        Integer id=passwordModifyWrap.getId();
        String newPwd=passwordModifyWrap.getNewPwd();
        String oldPwd=passwordModifyWrap.getOldPwd();
/*
        if (!Objects.equals(newPwd, againPwd)) {
            throw new IllegalArgumentException("二次密码不一样");
        }
*/
        int result=-1;
        GenericUser genericUser=genericUserService.getById(id)
//        GenericUser genericUser=genericUserService.getUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("无该用户信息"));
        if (!passwordEncoder.matches(oldPwd, genericUser.getPassword())) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("旧密码错误")
                    .withCode(50000)
                    .build();
        }
        try {
            result=genericUserService.modifyUserPasswordById(id, authentication.getName(), newPwd);
            return ResponseResult.forSuccessBuilder()
                    .withMessage("修改密码成功")
                    .withCode(20000)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.forFailureBuilder()
                    .withMessage("密码修改失败")
                    .withCode(50000).build();

        }
    }


    @PostMapping("/update")
    public ResponseResult editUser(@RequestBody UserDto userDto) {
        GenericUser targetUser=genericUserMapper.toTo(userDto);
        Optional<GenericUser> newUser=genericUserService.update(targetUser);
        return newUser.map(u->ResponseResult.forSuccessBuilder()
                .withMessage("修改成功")
                .withData("newUser", genericUserMapper.toDto(u))
                .build())
                .orElse(ResponseResult.forFailureBuilder()
                        .withMessage("修改失败").build());
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody UserDto userDto) {
        String username=userDto.getUsername();
        genericUserService.delete(userDto.getId());
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    @ExceptionHandler
    public ResponseResult handleError(Exception e) {
        String m=e.getMessage();
        return ResponseResult.forFailureBuilder()
                .withMessage(m==null ? m : "操作失败")
                .build();
    }

    private ResponseResult outputResult(String dm, String fm, GenericUser genericUser) {

        if (genericUser!=null) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage(dm).build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage(fm).build();
    }


}
