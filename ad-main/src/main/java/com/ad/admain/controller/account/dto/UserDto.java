package com.ad.admain.controller.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.UpdateIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String username;
    private String nickname;
    private String realname;
    private String idCard;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @UpdateIgnore
    private String password;

    private String avatar;

    private String wechat;
    private String intro;
    private String gender;

    private LocalDate birthday;
    private LocalDateTime regTime;
    private LocalDateTime lastModified;
    private String status;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private boolean certification;
    private LocalDateTime loginTime;

    private String mobilePhone;
    private String email;
    private String roles;

}
