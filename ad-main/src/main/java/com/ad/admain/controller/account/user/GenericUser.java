package com.ad.admain.controller.account.user;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.launch.user.AuthenticationEnum;
import com.ad.launch.user.IUser;
import com.wezhyn.project.BaseEnum;
import com.wezhyn.project.account.SexEnum;
import com.wezhyn.project.annotation.UpdateIgnore;
import com.wezhyn.project.utils.EnumUtils;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Table(name = "ad_generic_user", indexes = {
        @Index(name = "username_index", columnList = "username", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
public class GenericUser implements IUser {

    /*
            /**********************************************************
            /* 静态成员变量初始化
            /**********************************************************
        */
    static {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @UpdateIgnore
    @Column(unique = true)
    @ColumnDefault("''")
    private String username;
    @ColumnDefault("''")
    private String nickName;

    @UpdateIgnore
    @Column(nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'UNKNOWN'")
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
    @UpdateIgnore
    private String avatar;
    @Column(name = "birth_day", columnDefinition = "timestamp default current_timestamp")
    private LocalDate birthDay;
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'USER'")
    private AuthenticationEnum roles;

    /**
     * 控制用户登录状态，默认：未认证，但可以登录
     * 当 {@link UserEnable#getValue()} <0 时，代表用户状态异常,限制登录
     * {@link #isLock()}
     */
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'NOT_AUTHENTICATION'")
    private UserEnable enable;

    @ColumnDefault("''")
    private String realName;
    @ColumnDefault("''")
    private String idCard;


    @org.hibernate.annotations.Generated(value = GenerationTime.INSERT)
    @Column(insertable = false, updatable = false)
    @ColumnDefault("current_timestamp")
    private LocalDateTime regTime;
    @Column(insertable = false, updatable = false)
    @org.hibernate.annotations.Generated(
            value = GenerationTime.ALWAYS
    )
    @ColumnDefault("current_timestamp")
    private LocalDateTime loginTime;
    /**
     * 使用数据库生成的值，忽略前台传入
     */
    @Column(insertable = false, updatable = false)
    @org.hibernate.annotations.Generated(
            value = GenerationTime.ALWAYS
    )
    @ColumnDefault("current_timestamp")
    private LocalDateTime lastModified;


    @OneToMany(mappedBy = "orderUser", cascade = CascadeType.REMOVE)
    private List<Equipment> equipmentList;


    @Override
    public String getSex() {
        return this.sex == null ? SexEnum.UNKNOWN.getValue() : this.sex.getValue();
    }

    public void setSex(String sex) {
        this.sex = EnumUtils.valueOfStringEnumIgnoreCase(SexEnum.class, sex);
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    @Override
    public String getStatus() {
        return enable.getEnableStatus();
    }

    @Override
    public Boolean isLock() {
        return enable.getEnableNum() >= 0;
    }

    @Getter
    @AllArgsConstructor
    public static enum UserEnable implements BaseEnum {
        /**
         * 用户状态
         */
        NORMAL(1, "normal"),
        NOT_AUTHENTICATION(0, "authentication"),
        MODIFY_AUTHENTICATION(10, "modify"),
        FORBID(-1, "forbid");

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

    @Override
    public String toString() {
        return "GenericUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", email='" + email + '\'' +
                ", wechat='" + wechat + '\'' +
                ", intro='" + intro + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthDay=" + birthDay +
                ", roles=" + roles +
                ", enable=" + enable +
                ", realName='" + realName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", regTime=" + regTime +
                ", loginTime=" + loginTime +
                ", lastModified=" + lastModified +
                '}';
    }
}
