package com.ad.admain.cache;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.google.common.cache.Cache;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * @ClassName EquipmentCacheServiceTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 19:14
 * @Version V1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class EquipmentCacheServiceTest {
    @Autowired
    EquipmentCacheService equipmentCacheService;
    @Autowired
    EquipmentService equipmentService;
    String key = "383838438";
    @Test
    public void cacheTest() throws ExecutionException {
        Cache<String , Equipment> cache  = equipmentCacheService.getCache();


        Long t3 = System.currentTimeMillis();
        Equipment equipment1 = equipmentCacheService.getValue(key);
        Long t4 = System.currentTimeMillis();
        System.out.format("消耗:%d\n",t4-t3);
        System.out.println(equipment1.toString());

        Long t5 = System.currentTimeMillis();
        Equipment equipment2 = equipmentCacheService.getValue(key);
        Long t6 = System.currentTimeMillis();
        System.out.format("消耗:%d\n",t6-t5);
        System.out.println(equipment1.toString());
    }
    }






