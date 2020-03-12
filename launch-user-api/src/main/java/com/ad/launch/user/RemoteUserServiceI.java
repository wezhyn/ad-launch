package com.ad.launch.user;

import com.ad.launch.user.exception.NotUserException;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
public interface RemoteUserServiceI<T> {


    /**
     * 根据id 加载对应用户基本信息
     *
     * @param id id
     * @return 信息
     */
    AdUser loadUser(T id) throws NotUserException;
}
