package com.ad.admain.controller;

import com.ad.admain.controller.impl.ImgBed;
import com.ad.admain.controller.impl.ImgBedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wezhyn
 * @date 2019/09/29
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Repository
public interface ImgBedRepository extends JpaRepository<ImgBed, Integer> {

    /**
     * 发现图床
     *
     * @param type     类别
     * @param pageable 分页
     * @return list
     */
    Page<ImgBed> findAllByType(ImgBedType type, Pageable pageable);
}
