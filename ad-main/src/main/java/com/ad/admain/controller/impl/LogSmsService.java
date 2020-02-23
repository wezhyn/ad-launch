package com.ad.admain.controller.impl;

import com.ad.admain.controller.exception.SmsException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wezhyn
 * @since 02.23.2020
 */
@Slf4j
public class LogSmsService extends AbstractSmsService {
    @Override
    void doSend(String mobile, String code, SmsType type) throws SmsException {
        log.info("send code {} for {} to {}", code, type, mobile);
    }
}
