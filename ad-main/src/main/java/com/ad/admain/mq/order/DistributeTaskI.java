package com.ad.admain.mq.order;

import com.ad.admain.cache.PooledIdAndEquipCache;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.screen.entity.Task;

import java.util.HashMap;

/**
 * @ClassName DistributeTaskI
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 0:58
 * @Version V1.0
 **/
public interface DistributeTaskI {


    /**
     * @Description //发送任务信息到消息队列
     * @Date 2020/3/8 0:59
     * @param adOrder
     *@return void
     **/
    void sendTask(final AdOrder adOrder);


    /**
     * @Description //统计在线车辆数目
     * @Date 2020/3/8 16:46
     * @param
     *@return int
     **/
    int countOnlineNums();


    /**
     * @Description //返回空闲车辆的hashmap
     * @Date 2020/3/8 16:47
     * @param
     *@return java.util.HashMap<java.lang.Long,com.ad.admain.controller.equipment.entity.Equipment>
     **/
    HashMap<Long, PooledIdAndEquipCache> freeEquips();

    HashMap<Long,PooledIdAndEquipCache> scopeFreeEquips(Double longitude,Double latitude,Double scope);

    HashMap<Long,PooledIdAndEquipCache> scopeAvailableFreeEquips(Double longitude,Double latitude,Double scope,int rate);

}
