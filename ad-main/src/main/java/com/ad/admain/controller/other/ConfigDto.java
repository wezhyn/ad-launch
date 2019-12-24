package com.ad.admain.controller.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConfigDto {

    private Integer id;
    private String key;
    private String value;
    private String type;

}
