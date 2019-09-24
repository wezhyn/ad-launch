package com.ad.adlaunch.controller;

import com.ad.adlaunch.dto.Admin;
import com.ad.adlaunch.dto.GenericUser;
import com.ad.adlaunch.dto.ResponseResult;
import com.ad.adlaunch.dto.SimpleResponseResult;
import com.ad.adlaunch.service.AdminService;
import com.ad.adlaunch.to.AdminTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/getInfo")
    public SimpleResponseResult<AdminTo> info(@AuthenticationPrincipal Authentication authentication) {
        String name=authentication.getName();
        Admin admin =adminService.getById(name);
        return SimpleResponseResult.successResponseResult("", AdminTo.fromAdmin(admin));
    }

    @GetMapping("/getList")
    public ResponseResult getList(@RequestParam(name="limit",defaultValue="10") int limit, @RequestParam(name="page",defaultValue="1") int page) {
        Pageable pageable= PageRequest.of(page - 1, limit);
        Page<Admin> admins = adminService.getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", AdminTo.fromUserList(admins.getContent()))
                .withData("total", admins.getTotalElements())
                .build();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody AdminTo adminTo) {
        Admin admin=adminTo.toAdmin();
        admin=adminService.save(admin);
        if (admin!=null && admin.getId()!=null) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("注册成功").build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("注册失败").build();
    }

    @PostMapping("/edit")
    public ResponseResult editUser(@RequestBody AdminTo adminTo) {
        Admin oldUser=adminTo.toAdmin();
        Admin newUser=adminService.save(oldUser);
        if (oldUser!=null) {
            return ResponseResult.forSuccessBuilder()
                    .withMessage("修改成功")
                    .withData("newUser", AdminTo.fromAdmin(newUser))
                    .build();
        }
        return ResponseResult.forFailureBuilder()
                .withMessage("修改失败").build();
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody AdminTo adminTo) {
        String id=adminTo.getId();
        adminService.delete(id);
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
