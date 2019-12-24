package com.ad.admain.controller.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.annotation.UpdateIgnore;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class AdminDto implements IBaseTo<Integer> {


    private Integer id;

    @JsonProperty("username")
    private String username;
    @JsonProperty("nickname")
    private String nickName;

    @UpdateIgnore
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String idCard;
    private String avatar;
    private String sex;
    private String email;
    private String roles;

}
