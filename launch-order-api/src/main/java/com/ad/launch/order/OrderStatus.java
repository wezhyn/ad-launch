package com.ad.launch.order;

import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@AllArgsConstructor
public enum OrderStatus implements NumberEnum, StringEnum {

    /**
     * 三种完成状态：
     * 等待付款-> 取消
     * 等待付款 -> 付款成功 -> 退款
     * 等待付款 -> 付款成功 -> 执行中 -> 退款 (部分退款)
     * 等待付款 -> 付款成功 -> 执行完成
     */
    CANCEL(-10),
    REFUNDING(-3),
    REFUNDED(-2),
    WAITING_PAYMENT(0),
    SUCCESS_PAYMENT(1),
    WAITING_EXECUTION(2),
    EXECUTING(3),
    EXECUTION_COMPLETED(4);

    private int orderCode;

    @Override
    public Integer getNumber() {
        return orderCode;
    }

    @Override
    public String getValue() {
        return name().toLowerCase();
    }
}
