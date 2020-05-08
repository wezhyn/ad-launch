package com.ad.admain.controller.equipment.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @date 2019/10/31
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
@Builder
public class EquipmentDto {

    private Integer id;

    private String username;
    private Integer uid;
    private String intro;
    private String img;

    private String name;


    private boolean status;
    private String verify;

    private Double latitude;
    private Double longitude;
    @Length(min = 15, max = 15, message = "长度限制为15")
    private String key;
    private String feedback;


    private LocalDateTime startTime;

    private LocalDateTime endTime;


    private LocalDateTime createTime;
}
