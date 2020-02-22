package com.ad.admain.controller;

import com.ad.admain.controller.exception.SmsException;
import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 手机短信发送服务
 *
 * @author wezhyn
 * @since 02.22.2020
 */
public interface ISmsService {


    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     *
     * @param 手机号常规格式： 13+任意数
     *                 15+除4的任意数
     *                 18+除1和4的任意数
     *                 17+除9的任意数
     *                 147
     */
    static boolean verifyPhone(String str) throws PatternSyntaxException {
        String regExp="^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p=Pattern.compile(regExp);
        Matcher m=p.matcher(str);
        return m.matches();
    }

    /**
     * 向某个手机发送短信
     *
     * @param mobil 手机号
     * @param type  类型
     * @return 是否发送成功
     */
    SmsResponse send(String mobil, SmsType type) throws SmsException;

    /**
     * 为目标手机号创建一个短信校验码
     *
     * @param mobil 手机号
     * @param type  发送类型
     * @return “123456”
     */
    SmsResponse createVerificationCode(String mobil, SmsType type) throws SmsException;

    /**
     * 寻找目标手机号对应的验证码
     *
     * @param mobil 手机号
     * @param code  验证码
     * @return “123456"
     */
    Verify verifyCode(String mobil, String code, SmsType type) throws SmsException;

    enum SmsType implements StringEnum {
        /**
         * 短信类型
         */
        REGISTER, LOGIN, TRANSACTION;

        @Override
        public String getValue() {
            return name().toLowerCase();
        }
    }

    @AllArgsConstructor
    enum Verify implements StringEnum, NumberEnum {
        /**
         * 手机短信验证
         */
        SUCCESS(1, "创建验证码"),
        NONE(-1000, "无对应手机验证码"),
        REPEAT(-2000, "请勿反复创建验证码"),
        EXPIRED(-3000, "验证码过期"),
        MISMATCH(-4000, "验证码不匹配");
        private Integer verifyCode;
        private String verifyMsg;

        @Override
        public Integer getNumber() {
            return verifyCode;
        }

        @Override
        public String getValue() {
            return verifyMsg;
        }
    }

    interface SmsResponse {

        /**
         * 获取验证码
         *
         * @return "123456"
         */
        String getVerifyCode();

        /**
         * 获取对应验证码的手机号
         *
         * @return "12312312312"
         */
        String getPhoneMobil();

        /**
         * 获取结果
         *
         * @return 结果信息
         */
        Verify getVerify();


    }
}
