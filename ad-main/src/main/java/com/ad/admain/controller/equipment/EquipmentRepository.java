package com.ad.admain.controller.equipment;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.equipment.entity.EquipmentVerify;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

//    Page<Equipment> findAllByUid(Integer uid);

    Long countByLongitudeBetweenAndLatitudeBetween(Double minlgt,Double maxlgt,Double minlat,Double maxlat);

    Long countByStatusEqualsAndLongitudeBetweenAndLatitudeBetween(Boolean status,Double minlgt,Double maxlgt,Double minlat,Double maxlat);

    Long countAllByStatusEquals(Integer status);

    //统计区域内所有通过验证的在线车辆数目
    Long countByVerifyEqualsAndStatusEqualsAndLongitudeBetweenAndLatitudeBetween(EquipmentVerify equipmentVerify,Boolean status,Double minlgt, Double maxlgt, Double minlat, Double maxlat);

    //统计到区域内在线且审核通过的并且剩余的广告位多于需求频率的车辆数目
    Long countAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetweenAndVerifyEquals(Boolean status,Integer need,Double minlgt,Double maxlgt,Double minlat,Double maxlat,EquipmentVerify verify);

    //找出到区域内在线且审核通过的并且剩余的广告位多于需求频率的车辆
    List<Equipment> findAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetweenAndVerifyEqualsOrderByRemainDesc(Boolean status, Integer need, Double minlgt, Double maxlgt, Double minlat, Double maxlat, EquipmentVerify verity);

    Equipment findEquipmentByKeyEquals(String key);
}
