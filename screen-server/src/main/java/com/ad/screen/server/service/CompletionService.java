package com.ad.screen.server.service;

import com.ad.screen.server.entity.TaskKey;

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
     * @param taskKey key
     */
    void tryComplete(TaskKey taskKey);


    /**
     * 不等于总执行数量
     * <p>
     * 总执行数量=complete#exeNum+EquipTask#exeNum
     *
     * @param orderId order
     * @return int
     */
    Integer getOrderExecutedNumInComplete(Integer orderId);


}
