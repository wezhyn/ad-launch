package com.ad.admain.controller.quartz.service;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.OrderService;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.quartz.entity.JobEntity;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.ee.jmx.jboss.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicServiceImplTest {
    @Autowired
    DynamicJobService dynamicJobService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    OrderService orderService;

    Equipment equipment = null;
    Order order = null;
    @Before
    public void init(){
        equipment = equipmentService.getById(2).orElse(null);
        order = orderService.getById(60).orElse(null);
    }
    @Test
    public void insertOneJob() {
        JobEntity jobEntity = new JobEntity();
        jobEntity.setEquip(equipment)
                .setStatus("OPEN")
                .setOrder(order)
                .setName("test")
                .setJobGroup(order.getId().toString())
                .setDescription(order.getId()+":"+order.getId())
                .setAmount(10);
        dynamicJobService.insertOneJob(jobEntity);
    }

    @Test
    public void insertJobEntity() {
    }
}