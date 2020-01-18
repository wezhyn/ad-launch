package com.ad.admain.controller.distribute.dto;

import lombok.Data;

/**
 * @ClassName AssignmentDto
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/18 13:38
 * @Version 1.0
 */
@Data
public class AssignmentDto {
    private Integer id;
    private String[] content;
    private Integer equipId;
}
