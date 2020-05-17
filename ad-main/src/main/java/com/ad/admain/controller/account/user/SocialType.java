package com.ad.admain.controller.account.user;

import com.wezhyn.project.NumberEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@AllArgsConstructor
public enum SocialType implements NumberEnum {
    /**
     * 账户类型
     */
    ALIPAY(100), WECHAT(1000);

    private final Integer id;


    @Override
    public Integer getNumber() {
        return id;
    }
}
