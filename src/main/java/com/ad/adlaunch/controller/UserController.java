package com.ad.adlaunch.controller;

import com.ad.adlaunch.dto.GenericUser;
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
        GenericUser user=genericUserService.getById(name);
        return SimpleResponseResult.successResponseResult("", UserTo.fromGenericUser(user));
    }

    @GetMapping("/getList")
    public ResponseResult getList(@RequestParam(name="limit",defaultValue="10") int limit, @RequestParam(name="page",defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<GenericUser> genericUsers=genericUserService.getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", UserTo.fromGenericUserList(genericUsers.getContent()))
                .withData("total", genericUsers.getTotalElements())
                .build();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody UserTo userTo) {
        GenericUser genericUser=userTo.toGenericUser();
        genericUser=genericUserService.save(genericUser);
        if (genericUser!=null && genericUser.getId()!=null) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("注册成功").build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("注册失败").build();
    }

    @PostMapping("/edit")
    public ResponseResult editUser(@RequestBody UserTo userTo) {
        GenericUser oldUser=userTo.toGenericUser();
        GenericUser newUser=genericUserService.save(oldUser);
        if (oldUser!=null) {
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
        String username=userTo.getUsername();
        genericUserService.delete(username);
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    @ExceptionHandler
    public ResponseResult handleError(Exception e) {
        String m=e.getMessage();
        return ResponseResult.forFailureBuilder()
                .withMessage(m==null?m:"操作失败")
                .build();
    }

    private ResponseResult outputResult(String dM, String fM, GenericUser genericUser) {

        if (genericUser!=null ) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage(dM).build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage(fM).build();
    }


}
