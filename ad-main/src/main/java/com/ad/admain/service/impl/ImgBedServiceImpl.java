package com.ad.admain.service.impl;

import com.ad.admain.enumate.ImgBedType;
import com.ad.admain.repository.ImgBedRepository;
import com.ad.admain.service.AbstractBaseService;
import com.ad.admain.service.ImgBedService;
import com.ad.admain.to.ImgBed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author wezhyn
 * @date 2019/09/29
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class ImgBedServiceImpl extends AbstractBaseService<ImgBed, Integer> implements ImgBedService {


    @Autowired
    private ImgBedRepository imgBedRepository;

    @Override
    public JpaRepository<ImgBed, Integer> getRepository() {
        return imgBedRepository;
    }


    @Override
    public Page<ImgBed> getImgBedListByType(ImgBedType imgBedType, Pageable pa) {
        return imgBedRepository.findAllByType(imgBedType, pa);
    }
}
