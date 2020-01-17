package com.ad.admain.controller.distribute.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName TaskDto
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/16 22:38
 * @Version 1.0
 */
@Data
@Builder
public class TaskDto {
    private Integer id;
    private LocalDateTime startTime;
    private String content;
    private Long num;
    private Integer orderId;
}
