package com.ad.admain.controller.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.UpdateIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto implements IBaseTo<Integer> {


    private Integer id;
    @Pattern(regexp="^[a-zA-Z]{1}([a-zA-Z0-9]|){4,19}$", message="用户名应以字母开头")
    private String username;
    private String nickname;
    private String realname;
    private String idCard;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @UpdateIgnore
    @NotNull(message="密码不能为空")
    private String password;

    private String avatar;

    private String wechat;
    private String intro;
    private String gender;

    private LocalDate birthday;
    private LocalDateTime regTime;
    private LocalDateTime lastModified;
    private String status;
    private LocalDateTime loginTime;

    @Pattern(regexp="^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$",
            message="手机格式不匹配")
    private String mobilePhone;
    private String email;
    private String roles;

}
