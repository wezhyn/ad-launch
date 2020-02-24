package com.ad.admain.controller.impl;

import com.ad.admain.controller.ISmsService;
import com.ad.admain.controller.exception.SmsException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.KeyFactory;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
@SuppressWarnings("NullableProblems")
@Slf4j
public abstract class AbstractSmsService implements ISmsService {


    private final CacheKey KEY_FACTORY=(CacheKey) KeyFactory.create(CacheKey.class);

    private final Cache<Object, Sms> CACHE=CacheBuilder.newBuilder()
            .maximumSize(20000)
            .expireAfterAccess(120, TimeUnit.SECONDS)
            .build();

    protected String randomDigitalCode() {
        final int code=ThreadLocalRandom.current().nextInt(123456, 1000000);
        return String.valueOf(code);
    }

    @Override
    public SmsResponse send(String mobil, SmsType type) throws SmsException {
        SmsResponse sms=getVerifyCode(mobil, type);
        switch (sms.getVerify()) {
            case EXPIRED:
            case NONE: {
//                    则当前为代过期短信，重新创建一个
                sms=createVerificationCode(mobil, type);
                break;
            }
            case REPEAT: {
                throw new SmsException(Verify.REPEAT.getValue());
            }
            case SUCCESS: {
                break;
            }
            default: {
                throw new SmsException("系统异常");
            }
        }
        doSend(mobil, sms.getVerifyCode(), type);
        return sms;
    }

    @Override
    public SmsResponse createVerificationCode(String mobil, SmsType type) throws SmsException {
        if (!ISmsService.verifyPhone(mobil)) {
            throw new SmsException("手机格式错误");
        }
        final Sms sms=new Sms(randomDigitalCode(), mobil);
        CACHE.put(newKey(mobil, type), sms);
        return sms;
    }

    @Override
    public Verify verifyCode(String mobil, String code, SmsType type) throws SmsException {
        final SmsResponse sms=getVerifyCode(mobil, type);
        Verify currentVerify=sms.getVerify();
        if (currentVerify==Verify.SUCCESS) {
            if (Objects.equals(sms.getVerifyCode(), code)) {
                return Verify.SUCCESS;
            }
            return Verify.MISMATCH;
        }
        return currentVerify;
    }

    abstract void doSend(String mobile, String code, SmsType type) throws SmsException;

    /**
     * 默认当无验证码时会生成一个，有则直接返回
     *
     * @param mobile 手机号
     * @return sms
     */
    private SmsResponse getVerifyCode(String mobile, SmsType type) throws SmsException {
        try {
            return CACHE.get(newKey(mobile, type), ()->new Sms(randomDigitalCode(), mobile));
        } catch (ExecutionException e) {
            throw new SmsException(e);
        }
    }

    private Object newKey(String mobile, SmsType type) {
        return KEY_FACTORY.newInstance(mobile, type);
    }

    private interface CacheKey {

        /**
         * 创建一个代理 Key
         *
         * @param mobile 手机号
         * @param type   类型
         * @return key
         */
        Object newInstance(String mobile, SmsType type);
    }

}
