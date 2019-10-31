package com.ad.admain.to;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

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
public class Admin implements IAdmin {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @UpdateIgnore
    @Column(unique=true)
    private String username;


    private String nickName;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @UpdateIgnore
    private String password;

    private String idCard;

    private String avatar;

    @Enumerated(value=EnumType.STRING)
    @ColumnDefault("'UNKNOWN'")
    private SexEnum sex;

    private String email;


    @Enumerated(value=EnumType.STRING)
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
