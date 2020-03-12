package com.ad.admain.controller.equipment.impl;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 12.24.2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EquipmentServiceImplTest {

    @Autowired
    private EquipmentService equipmentService;

    @Test
    public void insert() {
        Equipment feed=Equipment.builder()
                .uid(1)
                .feedback("feed")
                .status(false)
                .build();
        equipmentService.save(feed);

    }

    @Test
    public void getEquipmentByRegion() {

        System.out.println(equipmentService.getEquipmentByRegion(120.58D, 30.01D, 100D));
    }
}