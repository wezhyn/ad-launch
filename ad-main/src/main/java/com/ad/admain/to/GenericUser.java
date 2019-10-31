package com.ad.admain.to;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.BaseEnum;
import com.ad.admain.enumate.SexEnum;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenerationTime;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @UpdateIgnore
    @Column(unique=true)
    private String username;

    private String nickName;
    private String realName;
    private String idCard;

    @UpdateIgnore
    private String password;


    @Enumerated(value=EnumType.STRING)
    private SexEnum sex;


    private String mobilePhone;
    private String email;
    private String wechat;
    private String intro;
    private String avatar;
    private LocalDate birthDay;

    @Enumerated(value=EnumType.STRING)
    @ColumnDefault(value="'user'")
    private AuthenticationEnum roles;


    @org.hibernate.annotations.Generated(value=GenerationTime.INSERT)
    @Column(insertable=false, updatable=false)
    @ColumnDefault("current_timestamp")
    private LocalDateTime regTime;

    private LocalDateTime loginTime;

    /**
     * 使用数据库生成的值，忽略前台传入
     */
    @Column(insertable=false, updatable=false)
    @org.hibernate.annotations.Generated(
            value=GenerationTime.ALWAYS
    )
    private LocalDateTime lastModified;


    /**
     * 控制用户登录状态
     */
    @Enumerated(value=EnumType.STRING)
    @ColumnDefault("'NORMAL'")
    private UserEnable enable;

    @ColumnDefault("true")
    private boolean accountNonExpired;
    @ColumnDefault("true")
    private boolean credentialsNonExpired;
    /**
     * 是否被锁定
     */
    @ColumnDefault("true")
    private boolean accountNonLocked;

    /**
     * 未清楚
     */
    @ColumnDefault("false")
    private boolean certification;

//    private LocalDateTime settledTime;

    @Override
    public String getSex() {
        return this.sex.getValue();
    }

    public User createAuthenticationUser(PasswordEncoder passwordEncoder) {
        boolean enableUser;
        switch (this.enable) {
            case FORBID: {
                enableUser=false;
                break;
            }
            default: {
                enableUser=true;
            }
        }
        return new User(username, passwordEncoder.encode(password), enableUser,
                accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities());
    }

    /*
        /**********************************************************
        /* 构造函数
        /**********************************************************
    */


    /*
        /**********************************************************
        /* 静态成员变量初始化
        /**********************************************************
    */
    static {
    }

    @Override
    public String getStatus() {
        return enable.getEnableStatus();
    }

    @Getter
    @AllArgsConstructor
    public static enum UserEnable implements BaseEnum {
        /**
         * 用户状态
         */
        NORMAL(1, "normal"),
        FORBID(-1, "forbed");

        private int enableNum;
        private String enableStatus;


        @Override
        public int getOrdinal() {
            return enableNum;
        }

        @Override
        public String getValue() {
            return enableStatus;
        }
    }
}
