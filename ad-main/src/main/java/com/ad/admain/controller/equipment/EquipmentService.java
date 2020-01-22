package com.ad.admain.controller.equipment;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.to.Order;
import com.wezhyn.project.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface EquipmentService extends BaseService<Equipment, Integer> {

    void quartzTest();

    Page<Equipment> getListByUid(Integer uid, Pageable pageable);
    /**
    * @title 根据投放位置经纬度统计方圆内的车辆数目
    * @description
    * @param square 范围半径
    * @author zlb
    * @updateTime 2020/1/16 23:37
    */
    Long getEquipmentByRegion(Double longitude,Double latitude,Double square);
    /**
    * @title 根据投放位置经纬度统计方圆内的在线数量
    * @description
    * @author zlb
    * @updateTime 2020/1/18 16:24
    * @throws
    */
    Long getAllOnlineEquipmentByRegion(Double longitude,Double latitude,Double square);

    Long countByStatusAndRegion(Integer need,Integer status,Double longitude,Double latitude,Double square);

    List<Equipment> findAllAvailableEquips(Integer need,Integer status,Double longitude,Double latitude,Double square,Integer size);
}
