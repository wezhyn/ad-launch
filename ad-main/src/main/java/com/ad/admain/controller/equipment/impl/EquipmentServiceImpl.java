package com.ad.admain.controller.equipment.impl;

import com.ad.admain.controller.equipment.EquipmentRepository;
import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class EquipmentServiceImpl extends AbstractBaseService<Equipment, Integer> implements EquipmentService {
    private static final int earthRadius = 6371; //地球半径


    @Autowired
    private EquipmentRepository equipmentRepository;


    @Override
    public Page<Equipment> getListByUid(Integer uid, Pageable pageable) {
        Equipment equipment=Equipment.createFromUid(uid);
        Example<Equipment> queryCondition=Example.of(equipment);
        return equipmentRepository.findAll(queryCondition, pageable);
    }

    @Override
    public Long getEquipmentByRegion(Double longitude, Double latitude, Double square) {
        Double dlng = 2*Math.asin(Math.sin(square/(2*earthRadius))/Math.cos(latitude*Math.PI/180));
        dlng = dlng*180/Math.PI; //角度转换为弧度
        Double dlat = square/earthRadius;
        dlat = dlat*180/Math.PI; //角度转换为弧度

    Double minlat = latitude - dlat; //最小经度
    Double maxlat = latitude + dlat; //最大经度
    Double minlgt = longitude -dlng; //最小纬度
    Double maxlgt = longitude + dlng; //最大纬度
        return equipmentRepository.countAllByLongitudeBetweenAndLatitudeBetween(minlgt,maxlgt,minlat,maxlat);
                }

@Override
public EquipmentRepository getRepository() {
        return equipmentRepository;
        }

        }
