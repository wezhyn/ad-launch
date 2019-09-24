package com.ad.adlaunch.controller;

import com.ad.adlaunch.service.BaseService;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public abstract class BaseController<T,ID> {


    /**
     * get service
     * @return
     */
    public abstract BaseService<T, ID> getService();
}
