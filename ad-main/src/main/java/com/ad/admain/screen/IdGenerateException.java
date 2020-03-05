package com.ad.admain.screen;

/**
 * @author wezhyn
 * @since 03.05.2020
 */
public class IdGenerateException extends RuntimeException {

    public IdGenerateException() {
        super("id 生成错误，系统异常");
    }
}
