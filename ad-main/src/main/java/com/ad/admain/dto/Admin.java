package com.ad.admain.dto;

import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

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

    public Admin() {
    }

    static {
        EMPTY_ADMIN=new Builder()
                .id(null)
                .nickName("")
                .build();
    }

    private Admin(Builder builder) {
        setId(builder.id);
        setNickName(builder.nickName);
        setPassword(builder.password);
        setIdCard(builder.idCard);
        setAvatar(builder.avatar);
        setSex(builder.sex);
        setEmail(builder.email);
        setRoles(builder.roles);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Admin copy) {
        Builder builder=new Builder();
        builder.id=copy.getId();
        builder.nickName=copy.getNickName();
        builder.password=copy.getPassword();
        builder.idCard=copy.getIdCard();
        builder.avatar=copy.getAvatar();
        builder.sex=copy.getSex();
        builder.email=copy.getEmail();
        builder.roles=copy.getRoles();
        return builder;
    }


    public static final class Builder {
        private String id;
        private String nickName;
        private String password;
        private String idCard;
        private String avatar;
        private SexEnum sex;
        private String email;
        private AuthenticationEnum roles;

        private Builder() {
        }

        public Builder id(String id) {
            this.id=id;
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

        public Admin build() {
            return new Admin(this);
        }
    }
}
