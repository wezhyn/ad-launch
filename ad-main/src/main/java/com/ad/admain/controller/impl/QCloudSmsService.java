package com.ad.admain.controller.impl;

import com.ad.admain.config.QqCloudSmsProperties;
import com.ad.admain.controller.exception.SmsException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
@Slf4j
public class QCloudSmsService extends AbstractSmsService {


    private final QqCloudSmsProperties qqCloudSmsProperties;

    public QCloudSmsService(QqCloudSmsProperties qqCloudSmsProperties) {
        this.qqCloudSmsProperties=qqCloudSmsProperties;
    }

    @Override
    void doSend(String mobile, String code, SmsType type) throws SmsException {
        final SmsSingleSender sender=new SmsSingleSender(qqCloudSmsProperties.getAppId(), qqCloudSmsProperties.getAppKey());
        int templateId;
        switch (type) {
            case REGISTER: {
                templateId=qqCloudSmsProperties.getRegisterTemplate();
                break;
            }
            case LOGIN: {
                templateId=qqCloudSmsProperties.getLoginTemplate();
                break;
            }
            default: {
                throw new SmsException("不支持的类型");
            }
        }
        try {
            sender.sendWithParam("86", mobile, templateId, new String[]{code},
                    qqCloudSmsProperties.getSmsSign(), "", "");

        } catch (IOException|HTTPException e) {
            throw new SmsException(e);
        }
    }
}
