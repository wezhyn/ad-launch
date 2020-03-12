package com.wezhyn.project.account;

import com.wezhyn.project.IBaseTo;

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
