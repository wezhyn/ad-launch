package com.ad.adlaunch.dto;

import com.ad.adlaunch.enumate.AuthenticationEnum;
import com.ad.adlaunch.enumate.SexEnum;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
@Builder
public class Admin implements IAdmin {

    public static final Admin EMPTY_ADMIN;

    @Id
    private String id;


    private String nickName;

    private String password;

    private String idCard;

    private String avatar;

    @Enumerated(value=EnumType.STRING)
    private SexEnum sex;

    private String email;


    private AuthenticationEnum roles;

    static {
        EMPTY_ADMIN=builder()
                .id(null)
                .nickName("")
                .build();
    }

}
