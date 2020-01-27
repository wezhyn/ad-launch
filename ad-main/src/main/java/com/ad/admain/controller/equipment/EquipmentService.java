package com.ad.admain.controller.equipment;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.equipment.entity.EquipmentVerify;
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
    //根据区域和在线状态统计车的数量
    Long countAllOnlineByRegionAndStatus(Boolean status,Double longitude,Double latitude,Double square);

    //统计目标区域内所有通过验证的在线车辆数目
    Long countAllByVerifyAndStatusAndRegion(Boolean status, Integer need, Double longitude, Double latitude, Double square,  EquipmentVerify equipmentVerify);

    //统计目标区域内所有通过验证符合订单需求的在线车辆数目
    Long countAllAvailableEquips(Boolean status,Integer need,Double longitude,Double latitude,Double square);

    //统计目标区域内符合订单需求的车辆
    List<Equipment> findAllAvailableEquips(Boolean status,Integer rate,Double longitude,Double latitude,Double square);
}
