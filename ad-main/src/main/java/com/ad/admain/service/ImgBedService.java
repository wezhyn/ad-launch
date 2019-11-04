package com.ad.admain.service;

import com.ad.admain.enumate.ImgBedType;
import com.ad.admain.to.ImgBed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author wezhyn
 * @date 2019/09/29
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface ImgBedService extends BaseService<ImgBed, Integer> {


    /**
     * 获取图床list
     *
     * @param imgBedType type
     * @param pageable   分页
     * @return list
     */
    Page<ImgBed> getImgBedListByType(ImgBedType imgBedType, Pageable pageable);


}
