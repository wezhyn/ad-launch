package com.ad.admain.common;

import com.ad.admain.enumate.StringEnum;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface BaseEnum extends StringEnum {
    /**
     * 返回当前序列
     * @return int
     */
    int getOrdinal();
}
