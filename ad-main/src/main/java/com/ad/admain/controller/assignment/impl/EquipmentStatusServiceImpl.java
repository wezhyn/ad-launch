package com.ad.admain.controller.assignment.impl;

import com.ad.admain.controller.assignment.EquipmentStatusRepository;
import com.ad.admain.controller.assignment.EquipmentStatusService;
import com.ad.admain.controller.assignment.entity.EquipmentStatus;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName EquipmentStatusServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/18 15:48
 * @Version 1.0
 */
public class EquipmentStatusServiceImpl extends AbstractBaseService<EquipmentStatus,Integer> implements EquipmentStatusService {
    @Autowired
    EquipmentStatusRepository equipmentStatusRepository;

    @Override
    public JpaRepository<EquipmentStatus, Integer> getRepository() {
        return equipmentStatusRepository;
    }
}
