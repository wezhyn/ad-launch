package com.ad.admain.controller.impl;

import com.ad.admain.controller.FileUploadService;
import com.ad.admain.controller.ImgBedRepository;
import com.ad.admain.controller.ImgBedService;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 当一个图片传进来时明确：index 与 key，可以认为是覆盖
     *
     * @param object 储存的对象
     * @return imgBed
     */
    @Override
    public ImgBed save(ImgBed object) {
        if (object.getIndex()!=null && object.getType()!=null) {
            Example<ImgBed> imgCondition=Example.of(ImgBed.builder()
                    .type(object.getType())
                    .index(object.getIndex()).build());

            Optional<ImgBed> imgBed=getRepository().findOne(imgCondition);
            imgBed.map(i->{
//                当此图片序列存在时
                object.setId(i.getId());
                fileUploadService.deleteFile(new QiNiuPutSet(i.getAddress()));
                return null;
            });
        }
        return super.save(object);
    }

    @Override
    public void delete(Integer integer) {
        Optional<ImgBed> imgBed=getRepository().findById(integer);
        imgBed.map(i->{
            super.delete(integer);
            fileUploadService.deleteFile(new QiNiuPutSet(i.getAddress()));
            return null;
        });
    }

    @Override
    public JpaRepository<ImgBed, Integer> getRepository() {
        return imgBedRepository;
    }

    @Override
    public Page<ImgBed> getImgBedListByType(ImgBedType imgBedType, Pageable pa) {
        return imgBedRepository.findAllByType(imgBedType, pa);
    }
}
