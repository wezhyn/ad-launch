package com.ad.screen.server.service;

/**
 * @ClassName CompletionI
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:47
 * @Version V1.0
 **/
public interface CompletionService {

    /**
     * 增加完成数据
     *
     * @param orderId  orderId
     * @param driverId driverId
     * @param num      Num
     */
    void completeNumIncr(int orderId, int driverId, int num);


    /**
     * 尝试标识该任务结束
     *
     * @param orderId  orderId
     * @param driverId driverId
     */
    void tryComplete(int orderId, int driverId);


}
