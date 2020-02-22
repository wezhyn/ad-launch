package com.ad.admain.controller.impl;

import com.ad.admain.controller.ISmsService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class Sms implements ISmsService.SmsResponse {

    private static final Integer EXPIRED_TIME=70;

    private static final Integer DELETE_TIME=120;

    private static final Integer INTERVAL_TIME=15;

    private String verifyCode;

    private String mobile;

    private ISmsService.Verify verify;

    private boolean error;

    private volatile boolean firstRead=true;

    private LocalDateTime createTime=LocalDateTime.now();

    /**
     * 正常创建的消息
     *
     * @param verifyCode 验证码
     * @param mobile     手机号
     */
    public Sms(String verifyCode, String mobile) {
        this.verifyCode=verifyCode;
        this.mobile=mobile;
        error=false;
    }

    /**
     * 构建错误的消息信息
     *
     * @param verifyCode 验证码
     * @param mobile     手机
     * @param verify     verify
     */
    public Sms(String verifyCode, String mobile, ISmsService.Verify verify) {
        this.verifyCode=verifyCode;
        this.mobile=mobile;
        this.verify=verify;
        error=true;
    }

    @Override
    public String getVerifyCode() {
        return verifyCode;
    }

    @Override
    public String getPhoneMobil() {
        return mobile;
    }

    @Override
    public ISmsService.Verify getVerify() {
        if (error) {
            return verify;
        } else {
            LocalDateTime now=LocalDateTime.now();
            final long seconds=ChronoUnit.SECONDS.between(createTime, now);
            if (seconds < INTERVAL_TIME && !firstRead) {
                firstRead=false;
                return ISmsService.Verify.REPEAT;
            } else if (seconds < EXPIRED_TIME) {
                firstRead=false;
                return ISmsService.Verify.SUCCESS;
            } else if (seconds < DELETE_TIME) {
                firstRead=false;
                return ISmsService.Verify.EXPIRED;
            } else {
                firstRead=false;
                return ISmsService.Verify.NONE;
            }
        }
    }
}
