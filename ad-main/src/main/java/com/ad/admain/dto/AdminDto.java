package com.ad.admain.dto;

import com.ad.admain.annotation.UpdateIgnore;
import com.ad.admain.common.IBaseTo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
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
