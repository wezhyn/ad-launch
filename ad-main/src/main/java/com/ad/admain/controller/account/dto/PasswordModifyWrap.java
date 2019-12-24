package com.ad.admain.controller.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordModifyWrap {
    private Integer id;
    private String oldPwd;
    private String newPwd;
}
