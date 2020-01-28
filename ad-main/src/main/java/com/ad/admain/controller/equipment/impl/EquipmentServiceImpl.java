package com.ad.admain.controller.equipment.impl;

import com.ad.admain.controller.equipment.EquipmentRepository;
import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.equipment.entity.EquipmentVerify;
import com.ad.admain.utils.SquareUtils;
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
      Double[] info = SquareUtils.getSquareInfo(longitude,latitude,square);
        return equipmentRepository.countByLongitudeBetweenAndLatitudeBetween(info[0],info[1],info[2],info[3]);
                }

    @Override
    public Long countAllByVerifyAndStatusAndRegion(EquipmentVerify equipmentVerify,Boolean status, Double longitude, Double latitude, Double square){
        Double[] info = SquareUtils.getSquareInfo(longitude,latitude,square);
        return equipmentRepository.countByVerifyEqualsAndStatusEqualsAndLongitudeBetweenAndLatitudeBetween(EquipmentVerify.PASSING_VERIFY,true,info[0],info[1],info[2],info[3]);
    }

    @Override
    public Long countAllOnlineByRegionAndStatus (Boolean status, Double longitude, Double latitude, Double square) {
        Double[] info = SquareUtils.getSquareInfo(longitude,latitude,square);
        Long nums = equipmentRepository.countByStatusEqualsAndLongitudeBetweenAndLatitudeBetween(status,info[0],info[1],info[2],info[3]);

        return nums;
    }

    @Override
    public Long countAllAvailableEquips(Boolean status, Integer rate, Double longitude, Double latitude, Double square) {
        Double[] info = SquareUtils.getSquareInfo(longitude,latitude,square);
        return equipmentRepository.countAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetweenAndVerifyEquals(true,rate,info[0],info[1],info[2],info[3],EquipmentVerify.PASSING_VERIFY);
    }

    @Override
    public List<Equipment> findAllAvailableEquips(Boolean status,Integer rate, Double longitude, Double latitude, Double square) {
        Double[] info = SquareUtils.getSquareInfo(longitude,latitude,square);
        return equipmentRepository.findAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetweenAndVerifyEqualsOrderByRemainDesc(true,rate,info[0],info[1],info[2],info[3], EquipmentVerify.PASSING_VERIFY);
    }


    @Override
    public EquipmentRepository getRepository() {
        return equipmentRepository;
    }
        }
