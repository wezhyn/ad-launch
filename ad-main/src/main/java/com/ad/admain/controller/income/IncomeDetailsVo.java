package com.ad.admain.controller.income;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wezhyn
 * @since 05.27.2021
 */
@Data
@NoArgsConstructor
public class IncomeDetailsVo {
    private Integer id;

    private Double amount;

    private LocalDateTime recordTime;

    private String message;

    public IncomeDetailsVo(Integer id, Double amount, LocalDateTime recordTime, String message) {
        this.id = id;
        this.amount = amount;
        this.recordTime = recordTime == null ? LocalDateTime.MIN : recordTime;
        this.message = message;
    }
}
