package com.ad.admain.controller.account;

import com.ad.admain.controller.ResponseResult;
import com.ad.admain.controller.SimpleResponseResult;
import com.ad.admain.controller.account.dto.AdminDto;
import com.ad.admain.controller.account.entity.Admin;
import com.ad.admain.convert.AdminMapper;
import com.ad.admain.security.AdAuthentication;
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

    private final AdminMapper adminMapper;

    public AdminController(AdminService adminService, AdminMapper adminMapper) {
        this.adminService=adminService;
        this.adminMapper=adminMapper;
    }


    @GetMapping("/info")
    public SimpleResponseResult<AdminDto> info(@AuthenticationPrincipal Authentication authentication) {
        String name=authentication.getName();
        Optional<Admin> admin=adminService.getByUsername(name);
        return admin.map(a->SimpleResponseResult.successResponseResult("", adminMapper.toDto(a)))
                .orElseGet(()->SimpleResponseResult.failureResponseResult("获取用户信息失败"));
    }

    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Admin> admins=adminService.getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", adminMapper.toDtoList(admins.getContent()))
                .withData("total", admins.getTotalElements())
                .build();
    }

    @PostMapping("/create")
    public ResponseResult register(@RequestBody AdminDto adminDto) {
        Admin requestAdmin=adminMapper.toTo(adminDto);
        Optional<Admin> savedAdmin=adminService.save(requestAdmin);
        return savedAdmin.map(a->ResponseResult.forSuccessBuilder().withMessage("注册成功").build())
                .orElseGet(()->ResponseResult.forFailureBuilder().withMessage("注册失败").build());
    }

    /**
     * 更新个人资料
     *
     * @param adminDto admin
     * @return
     */
    @PostMapping("/update")
    public ResponseResult editUser(@RequestBody AdminDto adminDto, @AuthenticationPrincipal AdAuthentication adAuthentication) {
        Admin oldUser=adminMapper.toTo(adminDto);
        oldUser.setId(adAuthentication.getId());
        Optional<Admin> newUser=adminService.update(oldUser);
        return newUser.map(a->ResponseResult.forSuccessBuilder()
                .withMessage("修改成功")
                .withData("newUser", adminMapper.toDto(newUser.get()))
                .build())
                .orElse(ResponseResult.forFailureBuilder()
                        .withMessage("修改失败").build());
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody AdminDto adminDto, @AuthenticationPrincipal AdAuthentication authentication) {
        Integer id=authentication.getId();
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
