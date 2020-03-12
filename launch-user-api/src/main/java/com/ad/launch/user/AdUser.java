package com.ad.launch.user;

import com.wezhyn.project.account.SexEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Dubbo 服务模型
 *
 * @author wezhyn
 * @since 03.12.2020
 */
public class AdUser implements Serializable {


    private static final long serialVersionUID=-6998397802374304042L;

    private Integer id;
    private String username;
    private String nickName;

    private SexEnum sex;
    private String mobilePhone;
    private String email;
    private String wechat;
    private String intro;
    private String avatar;
    private LocalDate birthDay;
    private AuthenticationEnum roles;


    private String realName;
    private String idCard;

    private LocalDateTime lastModified;

    public AdUser() {
    }

    public AdUser(Integer id, String username, String nickName, SexEnum sex, String mobilePhone, String email, String wechat, String intro, String avatar, LocalDate birthDay, AuthenticationEnum roles, String realName, String idCard, LocalDateTime lastModified) {
        this.id=id;
        this.username=username;
        this.nickName=nickName;
        this.sex=sex;
        this.mobilePhone=mobilePhone;
        this.email=email;
        this.wechat=wechat;
        this.intro=intro;
        this.avatar=avatar;
        this.birthDay=birthDay;
        this.roles=roles;
        this.realName=realName;
        this.idCard=idCard;
        this.lastModified=lastModified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName=nickName;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex=sex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone=mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat=wechat;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro=intro;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar=avatar;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay=birthDay;
    }

    public AuthenticationEnum getRoles() {
        return roles;
    }

    public void setRoles(AuthenticationEnum roles) {
        this.roles=roles;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName=realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard=idCard;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified=lastModified;
    }
}
