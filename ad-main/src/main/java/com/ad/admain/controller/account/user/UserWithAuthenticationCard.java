package com.ad.admain.controller.account.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 06.25.2020
 */
public interface UserWithAuthenticationCard {

    String getIdCard();

    String getIdCardAftImg();

    String getIdCardPreImg();

    String getRealName();

    Integer getId();


    String getUsername();

    String getNickname();

    String getRealname();

    String getEnable();

    String getPassword();

    String getAvatar();

    String getWechat();

    String getIntro();

    String getGender();

    LocalDate getBirthday();

    LocalDateTime getRegTime();

    LocalDateTime getLastModified();

    String getStatus();

    LocalDateTime getLoginTime();

    String getMobilePhone();

    String getEmail();

    String getRoles();
}

