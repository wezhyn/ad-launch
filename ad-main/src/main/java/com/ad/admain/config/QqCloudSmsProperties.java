package com.ad.admain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云 短信服务配置
 *
 * @author wezhyn
 * @since 02.22.2020
 */
@ConfigurationProperties(prefix="custom.sms")
@Data
public class QqCloudSmsProperties {
    private Integer appId;

    private String appKey;

    private Integer registerTemplate;

    private Integer loginTemplate;
    private String smsSign;
}
