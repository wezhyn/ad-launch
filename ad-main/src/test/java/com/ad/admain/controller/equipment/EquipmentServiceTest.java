package com.ad.admain.controller.equipment;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.equipment.entity.EquipmentVerify;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipmentServiceTest {
@Autowired
    EquipmentService equipmentService;
    @Test
    public void quartzTest() {
    }

    @Test
    public void getListByUid() {
    }

    @Test
    public void getEquipmentByRegion() {
        Long num = equipmentService.getEquipmentByRegion(120.044,30.226,100.000);
//        for (Equipment equipment :equipmentList){
//            System.out.println(equipment.toString());
//        }
        System.out.println(num);
    }

    @Test
    public void countAllOnlineByRegionAndStatus() {
        Long num = equipmentService.countAllOnlineByRegionAndStatus(true,120.044,30.226,100.000);
        System.out.println(num);
    }

    @Test
    public void countAllByVerifyAndStatusAndRegion() {
        Long num = equipmentService.countAllByVerifyAndStatusAndRegion(EquipmentVerify.PASSING_VERIFY,true,120.044,30.226,100.000);
        System.out.println(num);
    }

    @Test
    public void countAllAvailableEquips() {
        Long num = equipmentService.countAllAvailableEquips(true,21,120.044,30.226,100.000);
        System.out.println(num);
    }

    @Test
    public void findAllAvailableEquips() {
        List<Equipment> equipmentList = equipmentService.findAllAvailableEquips(true,20,120.044,30.226,100.000);
        System.out.println(equipmentList.toString());
    }
}