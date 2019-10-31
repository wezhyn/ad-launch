package com.ad.admain.dto;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.common.IBaseTo;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String sex;

    private LocalDate birthDay;
    private LocalDateTime regTime;
    private LocalDateTime lastModified;
    private String enable;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private boolean certification;
    private LocalDateTime loginTime;

    private String mobilePhone;
    private String email;
    private String roles;

}
