package com.ad.admain.controller.equipment;

import com.ad.admain.controller.equipment.entity.Equipment;
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

    Long countAllByLongitudeBetweenAndLatitudeBetween(Double minlgt,Double maxlgt,Double minlat,Double maxlat);

    Long countAllByLongitudeBetweenAndLatitudeBetweenAndStatusEquals(Double minlgt,Double maxlgt,Double minlat,Double maxlat,Integer status);

    Long countAllByStatusEquals(Integer status);

    Long countAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetween(Integer need,Integer status,Double minlgt,Double maxlgt,Double minlat,Double maxlat);

    List<Equipment> findAllByStatusEqualsAndRemainGreaterThanEqualAndLongitudeBetweenAndLatitudeBetween(Integer need, Integer status, Double minlgt, Double maxlgt, Double minlat, Double maxlat, Pageable pageable);
}
