package com.ad.admain.controller;

import com.ad.admain.service.BaseService;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public abstract class BaseController<T,ID> {


    /**
     * get service
     * @return baseService
     */
    public abstract BaseService<T, ID> getService();
}
