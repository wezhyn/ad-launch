package com.ad.adlaunch.to;

import com.ad.adlaunch.dto.Admin;
import com.ad.adlaunch.enumate.AuthenticationEnum;
import com.ad.adlaunch.enumate.SexEnum;
import com.ad.adlaunch.utils.EnumUtils;
import com.ad.adlaunch.utils.RoleAuthenticationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@Data
public class AdminTo {
    @JsonProperty("username")
    private String id;
    @JsonProperty("nickname")
    private String nickName;
    @JsonIgnore
    private String password;
    private String idCard;
    private String avatar;
    private SexEnum sex;
    private String email;
    private String[] roles;

    public static AdminTo fromAdmin(Admin admin){
        return AdminTo.builder()
                .id(admin.getId())
                .nickName(admin.getNickName())
                .password(admin.getPassword())
                .idCard(admin.getIdCard())
                .sex(admin.getSex())
                .email(admin.getEmail())
                .roles(RoleAuthenticationUtils.authentication2StringList(admin.getRoles()))
                .build();
    }

    public static List<AdminTo> fromUserList(Collection<Admin> admins) {
        return admins.stream()
                .map(AdminTo::fromAdmin)
                .collect(Collectors.toList());
    }


    public Admin toAdmin(){
        return Admin.newBuilder()
                .id(this.getId())
                .nickName(this.getNickName())
                .password(this.getPassword())
                .idCard(this.getId())
                .avatar(this.getAvatar())
                .sex(this.sex)
                .email(this.email)
                .roles(EnumUtils.valueOfBaseEnum(AuthenticationEnum.class,roles[0]))
                .build();

    }

}
