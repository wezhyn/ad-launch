package com.ad.admain.controller.quartz.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ModifyCronDto
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/26 11:37
 * @Version 1.0
 */
@Data
public class ModifyCronDTO {
    @NotNull(message = "the job id cannot be null")
    private Integer id;

    @NotEmpty(message = "the cron cannot be empty")
    private String cron;
}
