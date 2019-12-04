package com.ad.admain.controller.message;

import com.ad.admain.message.common.TargetTypeEnum;
import com.ad.admain.to.ResponseResult;
import io.swagger.client.model.Msg;
import io.swagger.client.model.UserName;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @date 2019/09/26
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/message")
@PreAuthorize("isAuthenticated()")
public class MessageController {


    @PostMapping("/users")
    public ResponseResult sendUsers(@RequestBody MsgContent msgContent,
                                    @AuthenticationPrincipal Authentication authentication) {

        Msg msg=new Msg()
                .targetType(TargetTypeEnum.USERS.toString())
                .target(msgContent.users);
        return ResponseResult.forSuccessBuilder()
                .build();

    }

    private static class MsgContent {
        //        arrayList<String> easemob出品
        private UserName users;
    }

}
