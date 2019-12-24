package com.ad.admain.message.common;

import io.swagger.client.ApiException;

/**
 * Created by easemob on 2017/3/16.
 */
@FunctionalInterface
public interface EasemobAPI {
    /**
     * 需要调用的接口
     *
     * @return object
     * @throws ApiException exception
     */
    Object invokeEasemobApi() throws ApiException;
}