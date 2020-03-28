package com.ad.screen.server.service;

import com.ad.screen.server.entity.MemoryCompletion;

/**
 * @ClassName CompletionI
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:47
 * @Version V1.0
 **/
public interface CompletionService {

    void completeIncrMemory(MemoryCompletion completion);


    Integer forOrderTotal(Integer orderId);

    /**
     * 获取并删除统计信息,并检查订单是否已经完成
     *
     * @param orderId  订单id
     * @param driverId 设备所有者id
     * @return 执行次数
     */
    Integer memoryToDisk(Integer orderId, Integer driverId);


}
