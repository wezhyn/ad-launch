package com.ad.admain.dto;

import com.ad.admain.common.IBaseTo;
import io.swagger.client.model.User;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Table(name="ad_easemob_user")
@Data
@Builder
public class EasemobUser implements IBaseTo<String> {

    public final static EasemobUser EMPTY_EASEMOB=EasemobUser.builder()
            .build();

    /**
     * 账号 {@link IUser#getId()}
     */
    @Id
    private String easemobId;

    private String uuid;
    private String created;
    private String modified;
    private boolean activated;


    /**
     * 用户昵称 {@link IUser#getNickName()}
     */
    private String nickname;
    private String password;

    @Override
    public String getId() {
        return easemobId;
    }

    public static User toRegisterUser(EasemobUser easemobUser) {
        return new User().password(easemobUser.getPassword())
                .username(easemobUser.getId());
    }


}
