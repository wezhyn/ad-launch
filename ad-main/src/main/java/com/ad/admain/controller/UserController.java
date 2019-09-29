package com.ad.admain.controller;

import com.ad.admain.dto.GenericUser;
import com.ad.admain.dto.ResponseResult;
import com.ad.admain.dto.SimpleResponseResult;
import com.ad.admain.service.GenericUserService;
import com.ad.admain.to.UserTo;
import com.ad.admain.utils.PropertyUtils;
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


    private final GenericUserService genericUserService;


    public UserController(GenericUserService genericUserService) {
        this.genericUserService = genericUserService;
    }

    @PostMapping("/test")
    public void updateTest(@RequestBody UserTo userTo) {
        System.out.println(userTo.toString());
    }

    @GetMapping("/getInfo")
    public SimpleResponseResult<UserTo> info(@AuthenticationPrincipal Authentication authentication) {
        String name = authentication.getName();
        GenericUser user = genericUserService.getById(name);
        return SimpleResponseResult.successResponseResult("", UserTo.fromGenericUser(user));
    }

    @GetMapping("/getList")
    public ResponseResult getList(@RequestParam(name = "limit", defaultValue = "10") int limit, @RequestParam(name = "page", defaultValue = "1") int page) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<GenericUser> genericUsers = genericUserService.getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", UserTo.fromGenericUserList(genericUsers.getContent()))
                .withData("total", genericUsers.getTotalElements())
                .build();
    }

    @PostMapping("/password")
    public ResponseResult editPassword(@RequestParam("username") String username,
                                       @RequestParam("oldpwd") String oldpwd,
                                       @RequestParam("newpwd")String newpwd) {
//        默认结果为失败 result==-1
        int result =-1;

        GenericUser genericUser = genericUserService.getById(username);
        if (genericUser.getPassword()!=oldpwd){
            return ResponseResult.forFailureBuilder()
                    .withMessage("旧密码错误")
                    .withCode(50000)
                    .build();
        }

        try {
            result = genericUserService.modifyUserPassword(genericUser.getId(),newpwd);
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


    public ResponseResult register(@RequestBody UserTo userTo) {
        GenericUser genericUser = userTo.toGenericUser();
        genericUser = genericUserService.save(genericUser);
        if (genericUser != null && genericUser.getId() != null) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("注册成功").build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("注册失败").build();
    }

    @PostMapping("/edit")
    public ResponseResult editUser(@RequestBody UserTo userTo) {
        GenericUser targetUser = userTo.toGenericUser();
        GenericUser newUser=genericUserService.update(targetUser);
        if (targetUser != null) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("修改成功")
                    .withData("newUser", UserTo.fromGenericUser(newUser))
                    .build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("修改失败").build();
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody UserTo userTo) {
        String username = userTo.getUsername();
        genericUserService.delete(username);
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    @ExceptionHandler
    public ResponseResult handleError(Exception e) {
        String m = e.getMessage();
        return ResponseResult.forFailureBuilder()
                .withMessage(m == null ? m : "操作失败")
                .build();
    }

    private ResponseResult outputResult(String dM, String fM, GenericUser genericUser) {

        if (genericUser != null) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage(dM).build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage(fM).build();
    }


}
