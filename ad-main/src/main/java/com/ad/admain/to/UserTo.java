package com.ad.admain.to;

import com.ad.admain.common.IBaseTo;
import com.ad.admain.dto.GenericUser;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import com.ad.admain.utils.EnumUtils;
import com.ad.admain.utils.RoleAuthenticationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Builder
@Data
public class UserTo implements IBaseTo<String > {


    private String username;
    private String nickname;
    private String realname;
    private String idCard;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String avatar;

    private String  sex;

    private LocalDate birthDay;

    private String mobilePhone;
    private String email;
    private String[] roles;

    @Override
    public String getId() {
        return getUsername();
    }

    public static UserTo fromGenericUser(GenericUser genericUser) {
        return UserTo.builder()
                .username(genericUser.getUsername())
                .roles(RoleAuthenticationUtils.authentication2StringList(genericUser.getRoles()))
                .nickname(genericUser.getNickName())
                .avatar(genericUser.getAvatar())
                .email(genericUser.getEmail())
                .birthDay(genericUser.getBirthDay())
                .idCard(genericUser.getIdCard())
                .realname(genericUser.getRealName())
                .mobilePhone(genericUser.getMobilePhone())
                .sex(genericUser.getSex())
                .build();
    }

    public static List<UserTo> fromGenericUserList(Collection<GenericUser> genericUsers) {
        return genericUsers.stream()
                .map(UserTo::fromGenericUser)
                .collect(Collectors.toList());
    }

    public GenericUser toGenericUser() {
        return GenericUser.newBuilder()
                .email(this.email)
                .sex(SexEnum.toSexEnum(this.sex))
                .password(password)
                .birthDay(birthDay)
                .mobilePhone(mobilePhone)
                .realName(realname)
                .nickName(nickname)
                .idCard(idCard)
                .avatar(avatar)
                .username(username)
                .userRole(EnumUtils.valueOfBaseEnum(AuthenticationEnum.class,this.roles[0]))
                .username(this.username).build();
    }

}
