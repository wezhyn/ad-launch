package com.ad.adlaunch.controller;

import com.ad.adlaunch.dto.GenericUser;
import com.ad.adlaunch.dto.IUser;
import com.ad.adlaunch.dto.ResponseResult;
import com.ad.adlaunch.dto.SimpleResponseResult;
import com.ad.adlaunch.service.GenericUserService;
import com.ad.adlaunch.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private GenericUserService genericUserService;

    @GetMapping("/getInfo")
    public SimpleResponseResult<UserTo> info(@AuthenticationPrincipal Authentication authentication) {
        String name=authentication.getName();
        IUser user=genericUserService.getUserByUserName(name);
        return SimpleResponseResult.successResponseResult("", UserTo.fromGenericUser((GenericUser) user));
    }

    @GetMapping("/getList")
    public ResponseResult getList(@RequestParam("limit")int limit,@RequestParam("page")int page) {
        Pageable pageable=PageRequest.of(page-1,limit);
        Page<GenericUser> genericUsers=genericUserService.getGenericList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", UserTo.fromGenericUserList(genericUsers.getContent()))
                .withData("total", genericUsers.getTotalElements())
                .build();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody UserTo userTo) {
        GenericUser genericUser=userTo.toGenericUser();
        genericUser=genericUserService.saveGenericUser(genericUser);
        if (genericUser!=null && genericUser.getUserId() > 0) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("注册成功").build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("注册失败").build();
    }

    @PostMapping("/editUser")
    public ResponseResult editUser(@RequestBody UserTo userTo) {
        GenericUser oldUser=userTo.toGenericUser();
        GenericUser newUser=genericUserService.saveGenericUser(oldUser);
        if (oldUser!=null && oldUser.getUserId() > 0) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("注册成功")
                    .withData("newUser",UserTo.fromGenericUser(newUser))
                    .build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("注册失败").build();
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody UserTo userTo) {
        String username=userTo.getUsername();
        int count=genericUserService.deleteGenericUser(username);
        if (count>0) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("删除成功").build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("删除失败").build();
    }
    private ResponseResult outputResult(String dM, String fM, GenericUser genericUser) {

        if (genericUser!=null && genericUser.getUserId() > 0) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage(dM).build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage(fM).build();
    }



}
