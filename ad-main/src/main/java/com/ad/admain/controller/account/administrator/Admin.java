package com.ad.admain.controller.account.administrator;

import com.ad.launch.user.AuthenticationEnum;
import com.ad.launch.user.IAdmin;
import com.wezhyn.project.account.SexEnum;
import com.wezhyn.project.annotation.UpdateIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.Date;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
@Table(name="ad_admin")
@Builder(builderClassName="Builder", builderMethodName="newBuilder")
@AllArgsConstructor
@Entity
@DynamicUpdate
@DynamicInsert
public class Admin implements IAdmin {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @UpdateIgnore
    @Column(unique=true)
    private String username;


    private String nickName;

    @UpdateIgnore
    private String password;

    private String idCard;

    private String mobilePhone;

    @UpdateIgnore
    private String avatar;

    @ColumnDefault("'UNKNOWN'")
    @Enumerated(value=EnumType.STRING)
    private SexEnum sex;

    @ColumnDefault("''")
    private String email;


    @Enumerated(value=EnumType.STRING)
    @ColumnDefault("'ADMIN'")
    private AuthenticationEnum roles;

    /**
     * 认证时间？？
     */
    @Generated(value=GenerationTime.INSERT)
    @Column(insertable=false, updatable=false)
    @ColumnDefault("current_timestamp")
    private Date authTime;

    public Admin() {
    }


}
