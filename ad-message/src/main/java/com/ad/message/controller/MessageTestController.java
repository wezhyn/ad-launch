package com.ad.message.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wezhyn
 * @date : 2019/09/25
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
public class MessageTestController {

    @GetMapping("/api/message")
    public String helloMessage() {
        return "hello message";
    }

}
