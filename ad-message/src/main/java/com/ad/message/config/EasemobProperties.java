package com.ad.message.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author : wezhyn
 * @date : 2019/09/25
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class EasemobProperties {
    public final static EasemobProperties EASEMOB_PROPERTIES=new EasemobProperties();

    private String host="a1.easemob.com";
    private String orgName;
    private String appName;
    private String clientId;
    private String clientSecret;
    private String grantType;

    private EasemobProperties() {
        InputStream inputStream = EasemobProperties.class.getClassLoader().getResourceAsStream("config.properties");
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        this.orgName=prop.getProperty("custom.emchat.org-name");
        this.appName=prop.getProperty("custom.emchat.app-name");
        this.grantType = prop.getProperty("custom.emchat.grant-type");
        this.clientId = prop.getProperty("custom.emchat.client-id");
        this.clientSecret = prop.getProperty("custom.emchat.client-secret");
        this.host=prop.getProperty("custom.emchat.host");
    }

    static {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host=host;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName=orgName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName=appName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId=clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret=clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType=grantType;
    }
}
