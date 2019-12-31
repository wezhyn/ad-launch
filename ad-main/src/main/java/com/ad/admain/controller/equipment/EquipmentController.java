package com.ad.admain.controller.equipment;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.equipment.dto.EquipmentDto;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.EquipmentMapper;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.controller.ResponseResult;
import com.wezhyn.project.exception.UpdateOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController extends AbstractBaseController<EquipmentDto, Integer, Equipment> {

    private final EquipmentMapper equipmentMapper;
    private EquipmentService equipmentService;


    public EquipmentController(EquipmentMapper equipmentMapper, EquipmentService equipmentService) {
        this.equipmentMapper=equipmentMapper;
        this.equipmentService=equipmentService;
    }

    @PostMapping("/verify")
    public ResponseResult verifyEquipment(@RequestBody EquipmentDto equipmentDto) {
        Equipment equipment=equipmentMapper.toTo(equipmentDto);
        Equipment updateEquipment=Equipment.builder()
                .verify(equipment.getVerify())
                .id(equipment.getId()).build();
        final Equipment savedEquipment=doUpdate(updateEquipment);
        return ResponseResult.forSuccessBuilder()
                .withMessage("验证成功")
                .withData("newTo", getConvertMapper().toDto(savedEquipment))
                .build();
    }

    @GetMapping("/list")
    @Override
    public ResponseResult listDto(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        return super.listDto(limit, page);
    }

    @Override
    @PostMapping("/create")
    public ResponseResult createTo(EquipmentDto entityDto) {
        return super.createTo(entityDto);
    }

    /**
     * 设置Equipment 所属的User
     *
     * @param to 要存储的实体类
     * @return equipment
     */
    @Override
    protected Equipment preSave(Equipment to) {
        final AdAuthentication authentication=(AdAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null) {
            throw new UpdateOperationException("当前用户未认证");
        }
        to.setUid(authentication.getId());
        return to;
    }

    @PostMapping("/delete")
    @Override
    public ResponseResult delete(@RequestBody EquipmentDto entityDto) {
        return super.delete(entityDto);
    }

    @Override
    public EquipmentService getService() {
        return equipmentService;
    }

    @Override
    public AbstractMapper<Equipment, EquipmentDto> getConvertMapper() {
        return equipmentMapper;
    }

    @GetMapping("/listByUid")
    public ResponseResult getUidList(@RequestParam(name="limit", defaultValue="10") int limit,
                                     @RequestParam(name="page", defaultValue="1") int page,
                                     @AuthenticationPrincipal AdAuthentication authentication) {
        int uid=authentication.getId();
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Equipment> equipmentList=getService().getListByUid(uid, pageable);
        return ResponseResult.forSuccessBuilder()
                .withList(getConvertMapper().toDtoList(equipmentList.getContent()), equipmentList.getTotalElements())
                .build();
    }
}
