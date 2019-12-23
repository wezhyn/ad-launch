package com.ad.admain.controller.equipment;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.wezhyn.project.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface EquipmentService extends BaseService<Equipment, Integer> {


    Page<Equipment> getListByUid(Integer uid, Pageable pageable);
}
