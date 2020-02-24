package com.ad.admain.controller.pay.to;

import com.wezhyn.project.NumberEnum;
import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@AllArgsConstructor
public enum OrderStatus implements NumberEnum {

    /**
     * 订单状态,具体修改状态见 {@link com.ad.admain.controller.pay.impl.OrderServiceImpl#modifyOrderStatus}
     */
    WAITING_PAYMENT(0),
    REFUNDING(-3),
    REFUNDED(-2),
    SUCCESS_PAYMENT(1),
    WAITING_EXECUTION(2),
    EXECUTING(3),
    EXECUTION_COMPLETED(4);

    private int orderCode;

    @Override
    public Integer getNumber() {
        return orderCode;
    }
}
