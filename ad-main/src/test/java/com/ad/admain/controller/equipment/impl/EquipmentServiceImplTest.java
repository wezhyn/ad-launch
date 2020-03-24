package com.ad.admain.controller.equipment.impl;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.equipment.entity.EquipmentVerify;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

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
        for (int count=0; count < 10; count++) {
            Equipment feed=Equipment.builder()
                    .uid(1)
                    .feedback("feed")
                    .intro("测试")
                    .createTime(LocalDateTime.of(2020, 1, 1, 1, 1))
                    .startTime(LocalDateTime.of(2020, 1, 1, 1, 1))
                    .endTime(LocalDateTime.of(2029, 12, 30, 23, 59))
                    .verify(EquipmentVerify.PASSING_VERIFY)
                    .name("test-equp-" + count)
                    .key("11111111111110" + count)
                    .remain(0)
                    .status(false)
                    .build();
            equipmentService.save(feed);
        }

    }

    @Test
    public void getEquipmentByRegion() {

        System.out.println(equipmentService.getEquipmentByRegion(120.58D, 30.01D, 100D));
    }
}