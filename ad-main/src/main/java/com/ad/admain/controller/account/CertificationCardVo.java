package com.ad.admain.controller.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 06.25.2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificationCardVo {

    private Integer id;
    @Pattern(regexp = "^[a-zA-Z]{1}([a-zA-Z0-9]|){4,19}$", message = "用户名应以字母开头")
    private String username;
    private String nickname;
    private String realname;

    private String avatar;

    private String wechat;
    private String intro;
    private String gender;

    private LocalDate birthday;
    private LocalDateTime regTime;
    private LocalDateTime lastModified;
    private String status;
    private LocalDateTime loginTime;

    @Pattern(regexp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$",
            message = "手机格式不匹配")
    private String mobilePhone;
    private String email;
    private String roles;


    private String idCard;
    private String idCardAftImg;
    private String idCardPreImg;
}
