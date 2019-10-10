package com.ad.admain.dto;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.common.IBaseTo;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import com.ad.admain.to.Admin;
import com.ad.admain.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@Data
public class AdminDto implements IBaseTo<String> {
    @JsonProperty("username")
    private String id;
    @JsonProperty("nickname")
    private String nickName;

    @UpdateIgnore
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String idCard;
    private String avatar;
    private SexEnum sex;
    private String email;
    private String roles;


    public static AdminDto fromAdmin(Admin admin) {
        return AdminDto.builder()
                .id(admin.getId())
                .nickName(admin.getNickName())
                .password(admin.getPassword())
                .idCard(admin.getIdCard())
                .sex(admin.getSex())
                .email(admin.getEmail())
                .roles(admin.getRoles().getValue())
                .build();
    }

    public static List<AdminDto> fromUserList(Collection<Admin> admins) {
        return admins.stream()
                .map(AdminDto::fromAdmin)
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
                .roles(EnumUtils.valueOfBaseEnumIgnoreCase(AuthenticationEnum.class, roles))
                .build();

    }

}
