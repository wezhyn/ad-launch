package com.ad.admain.controller.account.administrator;

import java.util.Optional;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.AdminMapper;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.BaseService;
import com.wezhyn.project.controller.NoNestResponseResult;
import com.wezhyn.project.controller.ResponseResult;
import com.wezhyn.project.exception.UpdateOperationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController extends AbstractBaseController<AdminDto, Integer, Admin> {

    private final AdminService adminService;

    private final AdminMapper adminMapper;

    public AdminController(AdminService adminService, AdminMapper adminMapper) {
        this.adminService=adminService;
        this.adminMapper=adminMapper;
    }


    @GetMapping("/info")
    public NoNestResponseResult<AdminDto> info(@AuthenticationPrincipal Authentication authentication) {
        String name=authentication.getName();
        Optional<Admin> admin=adminService.getByUsername(name);
        return admin.map(a->NoNestResponseResult.successResponseResult("", adminMapper.toDto(a)))
                .orElseGet(()->NoNestResponseResult.failureResponseResult("获取用户信息失败"));
    }

    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        return listDto(limit, page);
    }

    @Override
    @PostMapping("/create")
    public ResponseResult createTo(@RequestBody AdminDto entityDto) {
        return super.createTo(entityDto);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AdminDto entityDto) {
        return super.update(entityDto);
    }

    @Override
    protected Admin preUpdate(Admin to) {
        final AdAuthentication authentication = (AdAuthentication)SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null) {
            throw new UpdateOperationException("当前用户未认证");
        }
        if (to.getId() == null) {
            to.setId(authentication.getId());
        }
        return to;
    }

    @Override
    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody AdminDto entityDto) {
        return super.delete(entityDto);
    }


    @ExceptionHandler
    public ResponseResult handleError(Exception e) {
        String m=e.getMessage();
        return ResponseResult.forFailureBuilder()
                .withMessage(m!=null ? m : "操作失败")
                .build();
    }

    @Override
    public BaseService<Admin, Integer> getService() {
        return adminService;
    }

    @Override
    public AbstractMapper<Admin, AdminDto> getConvertMapper() {
        return adminMapper;
    }
}
