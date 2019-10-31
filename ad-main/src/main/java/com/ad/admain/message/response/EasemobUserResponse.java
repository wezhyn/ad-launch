package com.ad.admain.message.response;

import com.ad.admain.to.EasemobUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
public class EasemobUserResponse {

    private String action;
    private String application;
    private List<UserEntity> entities;
    private String timestamp;
    private int duration;
    private String organization;
    private String applicationName;

    private ErrorResponse errorResponse;

    /**
     * 返回的 EasemobUser不包含密码
     *
     * @return list
     */
    public List<EasemobUser> toEasemobUser() {
        if (errorResponse==null) {
            List<EasemobUser> easemobUsers=new ArrayList<>(2);
            for (UserEntity userEntity : this.entities) {
                easemobUsers.add(EasemobUser.builder()
                        .uuid(userEntity.getUuid())
                        .nickname(userEntity.getNickname())
                        .easemobId(Integer.parseInt(userEntity.getUsername()))
                        .activated(userEntity.isActivated())
                        .modified(userEntity.getModified())
                        .created(userEntity.getCreated())
                        .build()
                );
            }
            return easemobUsers;
        }
        return Collections.emptyList();
    }


    @Data
    private static class UserEntity {
        private String uuid;
        private String type="user";
        private String created;
        private String modified;
        private String username;
        private boolean activated;
        private String nickname;
    }


}
