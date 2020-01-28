package com.ad.admain.controller.equipment;

import com.ad.admain.controller.equipment.entity.Equipment;
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
        Long num = equipmentService.getEquipmentByRegion(30.2268,120.0445,50.0000);
//        for (Equipment equipment :equipmentList){
//            System.out.println(equipment.toString());
//        }
        System.out.println(num);
    }

    @Test
    public void countAllOnlineByRegionAndStatus() {
    }

    @Test
    public void countAllByVerifyAndStatusAndRegion() {
    }

    @Test
    public void countAllAvailableEquips() {
    }

    @Test
    public void findAllAvailableEquips() {
    }
}