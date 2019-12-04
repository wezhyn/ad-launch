package com.ad.admain.common;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public interface IUserTo<ID> extends IBaseTo<ID> {
    /**
     * 默认使用该值来查找用户/管理员，unique
     *
     * @return username
     */
    String getUsername();
}
