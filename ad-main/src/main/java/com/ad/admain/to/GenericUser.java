package com.ad.admain.to;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.BaseEnum;
import com.ad.admain.enumate.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Data
@DynamicUpdate
public class GenericUser implements IUser {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @UpdateIgnore
    @Column(unique=true)
    @ColumnDefault("''")
    private String username;

    @ColumnDefault("''")
    private String nickName;
    @ColumnDefault("''")
    private String realName;
    @ColumnDefault("''")
    private String idCard;

    @UpdateIgnore
    @Column(nullable=false)
    private String password;


    @Enumerated(value=EnumType.STRING)
    private SexEnum sex;


    @ColumnDefault("''")
    private String mobilePhone;
    @ColumnDefault("''")
    private String email;
    @ColumnDefault("''")
    private String wechat;
    @ColumnDefault("''")
    private String intro;
    @ColumnDefault("''")
    private String avatar;
    private LocalDate birthDay;

    @Enumerated(value=EnumType.STRING)
    @ColumnDefault(value="'USER'")
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

    private GenericUser(Builder builder) {
        setId(builder.id);
        setUsername(builder.username);
        setNickName(builder.nickName);
        setRealName(builder.realName);
        setIdCard(builder.idCard);
        setPassword(builder.password);
        setSex(builder.sex==null ? SexEnum.UNKNOWN : builder.sex);
        setMobilePhone(builder.mobilePhone);
        setEmail(builder.email);
        setWechat(builder.wechat);
        setIntro(builder.intro);
        setAvatar(builder.avatar);
        setBirthDay(builder.birthDay);
        setRoles(builder.roles==null ? AuthenticationEnum.CUSTOMER : builder.roles);
        setRegTime(builder.regTime);
        setLoginTime(builder.loginTime);
        setLastModified(builder.lastModified);
        setEnable(builder.enable);
        setAccountNonExpired(builder.accountNonExpired);
        setCredentialsNonExpired(builder.credentialsNonExpired);
        setAccountNonLocked(builder.accountNonLocked);
        setCertification(builder.certification);
    }

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

    public static Builder builder() {
        return new Builder();
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


    public static final class Builder {
        private Integer id;
        private String username;
        private String nickName;
        private String realName;
        private String idCard;
        private String password;
        private SexEnum sex;
        private String mobilePhone;
        private String email;
        private String wechat;
        private String intro;
        private String avatar;
        private LocalDate birthDay;
        private AuthenticationEnum roles;
        private LocalDateTime regTime;
        private LocalDateTime loginTime;
        private LocalDateTime lastModified;
        private UserEnable enable;
        private boolean accountNonExpired;
        private boolean credentialsNonExpired;
        private boolean accountNonLocked;
        private boolean certification;

        public Builder() {
        }

        public Builder id(Integer val) {
            id=val;
            return this;
        }

        public Builder username(String val) {
            username=val;
            return this;
        }

        public Builder nickName(String val) {
            if (val==null) {
                val=" ";
            }
            nickName=val;
            return this;
        }

        public Builder realName(String val) {
            if (val==null) {
                val=" ";
            }
            realName=val;
            return this;
        }

        public Builder idCard(String val) {
            if (val==null) {
                val=" ";
            }
            idCard=val;
            return this;
        }

        public Builder password(String val) {
            password=val;
            return this;
        }

        public Builder sex(SexEnum val) {
            if (val==null) {
                sex=SexEnum.UNKNOWN;
            }
            sex=val;
            return this;
        }

        public Builder mobilePhone(String val) {
            if (val==null) {
                val=" ";
            }
            mobilePhone=val;
            return this;
        }

        public Builder email(String val) {
            if (val==null) {
                val=" ";
            }
            email=val;
            return this;
        }

        public Builder wechat(String val) {
            if (val==null) {
                val=" ";
            }
            wechat=val;
            return this;
        }

        public Builder intro(String val) {
            if (val==null) {
                val=" ";
            }
            intro=val;
            return this;
        }

        public Builder avatar(String val) {
            if (val==null) {
                val=" ";
            }
            avatar=val;
            return this;
        }

        public Builder birthDay(LocalDate val) {
            birthDay=val;
            return this;
        }

        public Builder roles(AuthenticationEnum val) {
            if (val==null) {
                val=AuthenticationEnum.USER;
            }
            roles=val;
            return this;
        }

        public Builder regTime(LocalDateTime val) {
            regTime=val;
            return this;
        }

        public Builder loginTime(LocalDateTime val) {
            loginTime=val;
            return this;
        }

        public Builder lastModified(LocalDateTime val) {
            lastModified=val;
            return this;
        }

        public Builder enable(UserEnable val) {
            enable=val;
            return this;
        }

        public Builder accountNonExpired(boolean val) {
            accountNonExpired=val;
            return this;
        }

        public Builder credentialsNonExpired(boolean val) {
            credentialsNonExpired=val;
            return this;
        }

        public Builder accountNonLocked(boolean val) {
            accountNonLocked=val;
            return this;
        }

        public Builder certification(boolean val) {
            certification=val;
            return this;
        }

        public GenericUser build() {
            return new GenericUser(this);
        }
    }
}
