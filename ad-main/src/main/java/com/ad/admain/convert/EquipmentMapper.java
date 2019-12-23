package com.ad.admain.convert;

import com.ad.admain.controller.equipment.dto.EquipmentDto;
import com.ad.admain.controller.equipment.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Mapper(config=CentralMapperConfig.class,
        unmappedTargetPolicy=ReportingPolicy.IGNORE)
public interface EquipmentMapper extends AbstractMapper<Equipment, EquipmentDto> {
}
