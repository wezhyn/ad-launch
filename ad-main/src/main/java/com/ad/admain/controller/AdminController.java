package com.ad.admain.controller;

import com.ad.admain.dto.Admin;
import com.ad.admain.dto.GenericUser;
import com.ad.admain.dto.ResponseResult;
import com.ad.admain.dto.SimpleResponseResult;
import com.ad.admain.service.AdminService;
import com.ad.admain.to.AdminTo;
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

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService=adminService;
    }


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
