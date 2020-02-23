package com.wezhyn.project.controller;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 02.23.2020
 */
@AllArgsConstructor
public enum ResponseType implements StringEnum, NumberEnum {

    /**
     * 标识返回状态码和返回信息
     */
    SUCCESS(20000, "消息获取成功"),
    GENERIC_FAIL(50000, "请求异常"),
    LOGIN_EXCEPTION(50008, "登录异常"),
    NOT_FOUND(50004, "请求资源不存在，请检查请求路径"),
    NO_LOGIN_AUTHENTICATION(50009, "请检查是否正常登录"),
    SERVER_EXCEPTION(60000, "服务操作异常");

    private int code;
    private String msg;

    @Override
    public Integer getNumber() {
        return code;
    }

    @Override
    public String getValue() {
        return msg;
    }
}
