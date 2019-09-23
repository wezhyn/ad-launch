package com.ad.adlaunch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
public class HelloController {

    @GetMapping(value={"/","/api"})
    public String hello() {
        return "Hello";
    }

}
