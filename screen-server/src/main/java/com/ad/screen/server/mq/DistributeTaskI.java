package com.ad.screen.server.mq;


import com.ad.launch.order.AdOrder;
import com.ad.screen.server.cache.PooledIdAndEquipCache;

import java.util.HashMap;

/**
 * @ClassName DistributeTaskI
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 0:58
 * @Version V1.0
 **/
public interface DistributeTaskI {


//    /**
//     * @param adOrder
//     * @return void
//     * @Description //发送任务信息到消息队列
//     * @Date 2020/3/8 0:59
//     **/
//    void sendTask(final AdOrder adOrder);


    /**
     * @param
     * @return java.util.HashMap<java.lang.Long, com.ad.admain.controller.equipment.entity.Equipment>
     * @Description //返回空闲车辆的hashmap
     * @Date 2020/3/8 16:47
     **/
//    HashMap<Long, PooledIdAndEquipCache> freeEquips();
    HashMap<Long, PooledIdAndEquipCache> scopeEquips(Double longitude, Double latitude, Double scope);

//    HashMap<Long, PooledIdAndEquipCache> scopeFreeEquips(Double longitude, Double latitude, Double scope);

    HashMap<Long, PooledIdAndEquipCache> scopeAvailableFreeEquips(Double longitude, Double latitude, Double scope, int rate);

}
