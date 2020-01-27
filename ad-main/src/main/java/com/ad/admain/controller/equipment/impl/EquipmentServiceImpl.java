package com.ad.admain.controller.equipment.impl;

import com.ad.admain.controller.equipment.EquipmentRepository;
import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.equipment.entity.EquipmentVerify;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void quartzTest() {
        System.out.println("quartz test");
    }

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

        double minlat = latitude - dlat; //最小经度
        double maxlat = latitude + dlat; //最大经度
        double minlgt = longitude -dlng; //最小纬度
        double maxlgt = longitude + dlng; //最大纬度
        return equipmentRepository.countAllByLongitudeBetweenAndLatitudeBetween(minlgt,maxlgt,minlat,maxlat);
                }

    @Override
    public Long countAllByVerifyAndStatusAndRegion(Boolean status, Integer need, Double longitude, Double latitude, Double square,  EquipmentVerify equipmentVerify){
        double dlng = 2*Math.asin(Math.sin(square/(2*earthRadius))/Math.cos(latitude*Math.PI/180));
        dlng = dlng*180/Math.PI; //角度转换为弧度
        double dlat = square/earthRadius;
        dlat = dlat*180/Math.PI; //角度转换为弧度

        double minlat = latitude - dlat; //最小经度
        double maxlat = latitude + dlat; //最大经度
        double minlgt = longitude -dlng; //最小纬度
        double maxlgt = longitude + dlng; //最大纬度
        return equipmentRepository.countAllByStatusEqualsAndLongitudeBetweenAndLatitudeBetweenAndVerifyEquals(true,minlgt,maxlgt,minlat,maxlat,EquipmentVerify.PASSING_VERIFY);    }

    @Override
    public Long countAllOnlineByRegionAndStatus (Boolean status, Double longitude, Double latitude, Double square) {
        double dlng = 2*Math.asin(Math.sin(square/(2*earthRadius))/Math.cos(latitude*Math.PI/180));
        dlng = dlng*180/Math.PI; //角度转换为弧度
        Double dlat = square/earthRadius;
        dlat = dlat*180/Math.PI; //角度转换为弧度

        double minlat = latitude - dlat; //最小经度
        double maxlat = latitude + dlat; //最大经度
        double minlgt = longitude -dlng; //最小纬度
        double maxlgt = longitude + dlng; //最大纬度
        Long nums = equipmentRepository.countAllByLongitudeBetweenAndLatitudeBetweenAndStatusEquals(minlgt,maxlgt,minlat,maxlat,status);

        return nums;
    }

    @Override
    public Long countAllAvailableEquips(Boolean status, Integer rate, Double longitude, Double latitude, Double square) {
        double dlng = 2*Math.asin(Math.sin(square/(2*earthRadius))/Math.cos(latitude*Math.PI/180));
        dlng = dlng*180/Math.PI; //角度转换为弧度
        Double dlat = square/earthRadius;
        dlat = dlat*180/Math.PI; //角度转换为弧度

        double minlat = latitude - dlat; //最小经度
        double maxlat = latitude + dlat; //最大经度
        double minlgt = longitude -dlng; //最小纬度
        double maxlgt = longitude + dlng; //最大纬度
        return equipmentRepository.countAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetweenAndVerifyEquals(true,rate,minlgt,maxlgt,minlat,maxlat,EquipmentVerify.PASSING_VERIFY);
    }

    @Override
    public List<Equipment> findAllAvailableEquips(Boolean status,Integer rate, Double longitude, Double latitude, Double square) {
        Double dlng = 2*Math.asin(Math.sin(square/(2*earthRadius))/Math.cos(latitude*Math.PI/180));
        dlng = dlng*180/Math.PI; //角度转换为弧度
        Double dlat = square/earthRadius;
        dlat = dlat*180/Math.PI; //角度转换为弧度

        Double minlat = latitude - dlat; //最小经度
        Double maxlat = latitude + dlat; //最大经度
        Double minlgt = longitude -dlng; //最小纬度
        Double maxlgt = longitude + dlng; //最大纬度
        return equipmentRepository.findAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetweenAndVerifyEqualsOrderByRemainDesc(true,rate,minlgt,maxlgt,minlat,maxlat, EquipmentVerify.PASSING_VERIFY);
    }


    @Override
    public EquipmentRepository getRepository() {
        return equipmentRepository;
    }


        }
