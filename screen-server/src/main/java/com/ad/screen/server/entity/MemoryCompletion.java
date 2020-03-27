package com.ad.screen.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Task 小任务存统计
 *
 * @ClassName Completion
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:37
 * @Version V1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemoryCompletion implements ICompletion {

    /**
     * 执行的次数
     */
    Integer executedNum;
    /**
     * 订单的编号
     */
    Integer adOrderId;

    /**
     * 设备所有者id
     */
    Integer driverId;
}

