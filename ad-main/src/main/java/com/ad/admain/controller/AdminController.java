package com.ad.admain.controller;

import com.ad.admain.dto.AdminDto;
import com.ad.admain.service.AdminService;
import com.ad.admain.to.Admin;
import com.ad.admain.to.ResponseResult;
import com.ad.admain.to.SimpleResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService=adminService;
    }


    @GetMapping("/getInfo")
    public SimpleResponseResult<AdminDto> info(@AuthenticationPrincipal Authentication authentication) {
        String name=authentication.getName();
        Optional<Admin> admin=adminService.getById(name);
        return admin.map(a->SimpleResponseResult.successResponseResult("", AdminDto.fromAdmin(a)))
                .orElseGet(()->SimpleResponseResult.failureResponseResult("获取用户信息失败"));
    }

    @GetMapping("/getList")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Admin> admins=adminService.getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", AdminDto.fromUserList(admins.getContent()))
                .withData("total", admins.getTotalElements())
                .build();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody AdminDto adminDto) {
        Admin requestAdmin=adminDto.toAdmin();
        Optional<Admin> savedAdmin=adminService.save(requestAdmin);
        return savedAdmin.map(a->ResponseResult.forSuccessBuilder().withMessage("注册成功").build())
                .orElseGet(()->ResponseResult.forFailureBuilder().withMessage("注册失败").build());
    }

    @PostMapping("/edit")
    public ResponseResult editUser(@RequestBody AdminDto adminDto) {
        Admin oldUser=adminDto.toAdmin();
        Optional<Admin> newUser=adminService.save(oldUser);
        return newUser.map(a->ResponseResult.forSuccessBuilder()
                .withMessage("修改成功")
                .withData("newUser", AdminDto.fromAdmin(newUser.get()))
                .build())
                .orElse(ResponseResult.forFailureBuilder()
                        .withMessage("修改失败").build());
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody AdminDto adminDto) {
        String id=adminDto.getId();
        adminService.delete(id);
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    @ExceptionHandler
    public ResponseResult handleError(Exception e) {
        String m=e.getMessage();
        return ResponseResult.forFailureBuilder()
                .withMessage(m!=null ? m : "操作失败")
                .build();
    }

}
