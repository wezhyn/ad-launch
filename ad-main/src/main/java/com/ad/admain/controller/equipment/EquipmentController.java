package com.ad.admain.controller.equipment;

import com.ad.admain.convert.EquipmentMapper;
import com.ad.admain.dto.EquipmentDto;
import com.ad.admain.security.AdAuthentication;
import com.ad.admain.to.Equipment;
import com.ad.admain.to.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private EquipmentService equipmentService;


    @PostMapping("/create")
    public ResponseResult register(@RequestBody EquipmentDto equipmentDto, @AuthenticationPrincipal AdAuthentication authentication) {
        Equipment equipment=equipmentMapper.toTo(equipmentDto);
        equipment.setUid(authentication.getId());
        Optional<Equipment> genericUser=getService().save(equipment);
        return genericUser.map(u->ResponseResult.forSuccessBuilder().withMessage("设备添加成功").build())
                .orElseGet(()->ResponseResult.forFailureBuilder().withMessage("设备添加失败").build());
    }

    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Equipment> equipmentList=getService().getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", equipmentMapper.toDtoList(equipmentList.getContent()))
                .withData("total", equipmentList.getTotalElements())
                .build();
    }

    @GetMapping("/listByUid")
    public ResponseResult getUidList(@RequestParam(name="limit", defaultValue="10") int limit,
                                     @RequestParam(name="page", defaultValue="1") int page,
                                     @AuthenticationPrincipal AdAuthentication authentication) {
        int uid=authentication.getId();
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Equipment> equipmentList=getService().getListByUid(uid, pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", equipmentMapper.toDtoList(equipmentList.getContent()))
                .withData("total", equipmentList.getTotalElements())
                .build();
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody EquipmentDto equipmentDto) {
        getService().delete(equipmentDto.getId());
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    public EquipmentService getService() {
        return equipmentService;
    }
}
