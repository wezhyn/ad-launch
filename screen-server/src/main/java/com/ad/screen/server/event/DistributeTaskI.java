package com.ad.screen.server.event;


import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.mq.PrepareTaskMessage;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName DistributeTaskI
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 0:58
 * @Version V1.0
 **/
public interface DistributeTaskI {


    /**
     * 获取可用数量车，检查： 范围，剩余可用数量
     *
     * @param taskMessage 订单信息
     * @return 分配的设备
     */
    List<PooledIdAndEquipCache> availableEquips(PrepareTaskMessage taskMessage);

    Optional<PooledIdAndEquipCache> availableSingleEquip(PrepareTaskMessage taskMessage);


}
