package com.ad.admain.to;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

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
@Entity
@DynamicUpdate
public class Admin implements IAdmin {


    @Id
    private String username;


    private String nickName;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @UpdateIgnore
    private String password;

    private String idCard;

    private String avatar;

    @Enumerated(value=EnumType.STRING)
    private SexEnum sex;

    private String email;


    @Enumerated(value=EnumType.STRING)
    private AuthenticationEnum roles;

    private Date authTime;
    public Admin() {
    }


    private Admin(Builder builder) {
        setUsername(builder.username);
        setNickName(builder.nickName);
        setPassword(builder.password);
        setIdCard(builder.idCard);
        setAvatar(builder.avatar);
        setSex(builder.sex);
        setEmail(builder.email);
        setRoles(builder.roles);
        setAuthTime(builder.authTime);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Admin copy) {
        Builder builder=new Builder();
        builder.username=copy.getUsername();
        builder.nickName=copy.getNickName();
        builder.password=copy.getPassword();
        builder.idCard=copy.getIdCard();
        builder.avatar=copy.getAvatar();
        builder.sex=copy.getSex();
        builder.email=copy.getEmail();
        builder.roles=copy.getRoles();
        builder.authTime=copy.getAuthTime();
        return builder;
    }

    @Override
    public String getId() {
        return this.username;
    }


    public static final class Builder {
        private String username;
        private String nickName;
        private String password;
        private String idCard;
        private String avatar;
        private SexEnum sex;
        private String email;
        private AuthenticationEnum roles;
        private Date authTime;

        private Builder() {
        }

        public Builder id(String username) {
            this.username=username;
            return this;
        }

        public Builder nickName(String nickName) {
            this.nickName=nickName;
            return this;
        }

        public Builder password(String password) {
            this.password=password;
            return this;
        }

        public Builder idCard(String idCard) {
            this.idCard=idCard;
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

        public Builder email(String email) {
            this.email=email;
            return this;
        }

        public Builder roles(AuthenticationEnum roles) {
            this.roles=roles;
            return this;
        }
        public Builder authTime(Date authTime){
            this.authTime = authTime;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
}
