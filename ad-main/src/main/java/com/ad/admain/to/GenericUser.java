package com.ad.admain.to;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Table(name="ad_generic_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@DynamicUpdate
public class GenericUser implements IUser {


    @Id
    @UpdateIgnore
    private String username;

    private String nickName;
    private String realName;
    private String idCard;

    @UpdateIgnore
    private String password;

    private String avatar;

    @Enumerated(value=EnumType.STRING)
    private SexEnum sex;

    private LocalDate birthDay;

    private String mobilePhone;
    private String email;

    @Enumerated(value=EnumType.STRING)
    private AuthenticationEnum roles;


    @Override
    public String getId() {
       return getUsername();
    }

    @Override
    public String getSex() {
        return this.sex.getValue();
    }

    /*
        /**********************************************************
        /* 静态成员变量初始化
        /**********************************************************
    */
    static {
    }
}
