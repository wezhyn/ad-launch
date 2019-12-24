package com.ad.admain.message.response;

import lombok.Data;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
public class ErrorResponse {

    private String error;
    private String timestamp;
    private int duration;
    private String exception;
    private String errorDescription;
}
