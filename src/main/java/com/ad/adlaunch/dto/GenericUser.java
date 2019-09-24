package com.ad.adlaunch.dto;

import com.ad.adlaunch.enumate.AuthenticationEnum;
import com.ad.adlaunch.enumate.SexEnum;
import com.ad.adlaunch.utils.EnumUtils;
import com.ad.adlaunch.utils.RoleAuthenticationUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Table(name="ad_generic_user")
@NoArgsConstructor
@Data
public class GenericUser implements IUser {

    /**
     * 默认的空 User 对象
     */
    public static final GenericUser EMPTY_USER;


    @Id
    private String username;

    private String nickName;
    private String realName;
    private String idCard;

    private String password;

    private String avatar;

    @Enumerated(value=EnumType.STRING)
    private SexEnum sex;

    private LocalDate birthDay;

    private String mobilePhone;
    private String email;


    private AuthenticationEnum userRole;


    @Override
    public String getId() {
       return getUsername();
    }

    @Override
    public String getSex() {
        return this.sex.getValue();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleAuthenticationUtils.forGrantedAuthorities(this.userRole);
    }

    /*
        /**********************************************************
        /* builder 模式
        /**********************************************************
    */

    private GenericUser(Builder builder) {
        setUsername(builder.username);
        setNickName(builder.nickName);
        setRealName(builder.realName);
        setIdCard(builder.idCard);
        setPassword(builder.password);
        setAvatar(builder.avatar);
        setSex(builder.sex);
        setBirthDay(builder.birthDay);
        setMobilePhone(builder.mobilePhone);
        setEmail(builder.email);
        setUserRole(builder.userRole);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(GenericUser copy) {
        Builder builder=new Builder();
        builder.username=copy.getUsername();
        builder.nickName=copy.getNickName();
        builder.realName=copy.getRealName();
        builder.idCard=copy.getIdCard();
        builder.password=copy.getPassword();
        builder.avatar=copy.getAvatar();
        builder.sex=EnumUtils.valueOfBaseEnum(SexEnum.class, copy.getSex());
        builder.birthDay=copy.getBirthDay();
        builder.mobilePhone=copy.getMobilePhone();
        builder.email=copy.getEmail();
        builder.userRole=copy.getUserRole();
        return builder;
    }

    public static final class Builder {
        private Long userId;
        private String username;
        private String nickName;
        private String realName;
        private String idCard;
        private String password;
        private String avatar;
        private SexEnum sex;
        private LocalDate birthDay;
        private String mobilePhone;
        private String email;
        private AuthenticationEnum userRole;

        private Builder() {
        }

        public Builder userId(Long userId) {
            this.userId=userId;
            return this;
        }

        public Builder username(String username) {
            this.username=username;
            return this;
        }

        public Builder nickName(String nickName) {
            this.nickName=nickName;
            return this;
        }

        public Builder realName(String realName) {
            this.realName=realName;
            return this;
        }

        public Builder idCard(String idCard) {
            this.idCard=idCard;
            return this;
        }

        public Builder password(String password) {
            this.password=password;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar=avatar;
            return this;
        }

        public Builder sex(SexEnum sex) {
            this.sex=sex;
            return this;
        }

        public Builder birthDay(LocalDate birthDay) {
            this.birthDay=birthDay;
            return this;
        }

        public Builder mobilePhone(String mobilePhone) {
            this.mobilePhone=mobilePhone;
            return this;
        }

        public Builder email(String email) {
            this.email=email;
            return this;
        }

        public Builder userRole(AuthenticationEnum userRole) {
            this.userRole=userRole;
            return this;
        }

        public GenericUser build() {
            return new GenericUser(this);
        }
    }

    /*
        /**********************************************************
        /* 静态成员变量初始化
        /**********************************************************
    */
    static {
        EMPTY_USER=GenericUser.newBuilder()
                .userId(-1L)
                .username("")
                .email("").build();
    }
}
