package com.ad.admain.service;

import com.ad.admain.dto.ImgBed;
import com.ad.admain.enumate.ImgBedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author wezhyn
 * @date 2019/09/29
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface ImgBedService extends BaseService<ImgBed, Integer> {


    Page<ImgBed> getImgBedListByType(ImgBedType imgBedType, Pageable pageable);

}
