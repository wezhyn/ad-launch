package com.ad.admain.controller.income;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wezhyn
 * @since 05.27.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDetailsVo {
    private Integer id;

    private Double amount;

    private LocalDateTime recordTime;

    private String message;
}
