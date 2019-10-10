package com.ad.admain.controller;

import com.ad.admain.service.BaseService;
import com.ad.admain.to.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public abstract class BaseController<T,ID> {


    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<T> admins=getService().getList(pageable);
        return ResponseResult.forSuccessBuilder()
//                .withData("items", AdminDto.fromUserList(admins.getContent()))
                .withData("total", admins.getTotalElements())
                .build();
    }

    /**
     * get service
     * @return baseService
     */
    public abstract BaseService<T, ID> getService();
}
